package pl.marpiec.socnet.service.user

import pl.marpiec.socnet.database.UserDatabase
import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.DataStore
import pl.marpiec.util.{PasswordUtil, Strings, UID}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserQueryImpl(val socnetDatabase:UserDatabase, val dataStore:DataStore) extends UserQuery {

  def getUserById(id: UID):User = {
    dataStore.getEntity(classOf[User], id).asInstanceOf[User]
  }

  def getUserByCredentials(username: String, password: String):Option[User] = {

    val userOption = socnetDatabase.getUserByEmail(username)
    
    if (userOption.isDefined) {

      val user = userOption.get;
      val passwordHash = PasswordUtil.hashPassword(password, user.passwordSalt)

      if (Strings.equal(user.passwordHash, passwordHash)) {
        return userOption
      }
    }
    None
  }

}
