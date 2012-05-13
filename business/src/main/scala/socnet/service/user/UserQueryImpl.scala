package pl.marpiec.socnet.service.user

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.DataStore
import pl.marpiec.util.{PasswordUtil, UID}
import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.UserDatabase

/**
 * ...
 * @author Marcin Pieciukiewicz
 */


@Service("userQuery")
class UserQueryImpl @Autowired() (val socnetDatabase:UserDatabase, val dataStore:DataStore) extends UserQuery {

  def getUserById(id: UID):User = {
    dataStore.getEntity(classOf[User], id).asInstanceOf[User]
  }

  def getUserByCredentials(username: String, password: String):Option[User] = {

    val userOption = socnetDatabase.getUserByEmail(username)
    
    if (userOption.isDefined) {

      val user = userOption.get;
      val passwordHash = PasswordUtil.hashPassword(password, user.passwordSalt)

      if (StringUtils.equals(user.passwordHash, passwordHash)) {
        return userOption
      }
    }
    None
  }

}
