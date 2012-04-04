package pl.marpiec.socnet.service.user

import pl.marpiec.util.Strings
import pl.marpiec.socnet.database.UserDatabase
import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.DataStore

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserQueryImpl(val socnetDatabase:UserDatabase, val dataStore:DataStore) extends UserQuery {

  def getUserById(id: Int):User = {
    dataStore.getEntity(classOf[User], id).asInstanceOf[User]
  }

  def getUserByCredentials(username: String, password: String):User = {

    val user: User = socnetDatabase.getUserByEmail(username)
    if(user!=null && Strings.equal(user.password, password)) {
      user
    } else {
      null
    }
  }


}
