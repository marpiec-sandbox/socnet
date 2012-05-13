package pl.marpiec.socnet.readdatabase

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Repository("userDatabase")
class UserDatabaseMockImpl @Autowired() (dataStore: DataStore) extends AbstractDatabase[User](dataStore) with UserDatabase {

  val NAME_INDEX: String = "email"

  startListeningToDataStore(dataStore, classOf[User])

  addIndex(NAME_INDEX, (aggregate: Aggregate) => {
    val user = aggregate.asInstanceOf[User]
    user.email
  });

  def addUser(user: User) = add(user)

  def updateUser(user: User) = addOrUpdate(user)

  def getUserByEmail(email: String) = getByIndex(NAME_INDEX, email)

  def getUserById(id: UID) = getById(id)

  def findUser(query: String):List[User] = {
    //TODO this is stub implementation
    super.getAll
  }
}
