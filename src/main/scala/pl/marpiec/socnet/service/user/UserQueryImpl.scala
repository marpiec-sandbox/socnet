package pl.marpiec.socnet.service.user

import pl.marpiec.util.Strings
import pl.marpiec.socnet.database.UserDatabase
import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.DataStore
import java.util.UUID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserQueryImpl(val socnetDatabase:UserDatabase, val dataStore:DataStore) extends UserQuery {

  def getUserById(uuid: UUID):User = {
    dataStore.getEntity(classOf[User], uuid).asInstanceOf[User]
  }

  def getUserByCredentials(username: String, password: String):Option[User] = {

    val userOption = socnetDatabase.getUserByEmail(username)
    
    if (userOption.isDefined) {
      if (Strings.equal(userOption.get.password, password)) {
        return userOption
      }
    }
    None
  }

}
