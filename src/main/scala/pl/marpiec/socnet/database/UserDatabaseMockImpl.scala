package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{CqrsEntity, DataStore}
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserDatabaseMockImpl(dataStore: DataStore) extends AbstractDatabase[User](dataStore) with UserDatabase {

  val NAME_INDEX: String = "email"

  startListeningToDataStore(dataStore, classOf[User])

  addIndex(NAME_INDEX, (entity: CqrsEntity) => {
    val user = entity.asInstanceOf[User]
    user.email
  });

  def addUser(user: User) = add(user)

  def updateUser(user: User) = addOrUpdate(user)

  def getUserByEmail(email: String) = getByIndex(NAME_INDEX, email)

  def getUserById(id: UID) = getById(id)
}
