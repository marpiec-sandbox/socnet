package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.User
import collection.mutable.HashMap
import pl.marpiec.socnet.service.user.exception.UserAlreadyRegisteredException
import pl.marpiec.util.Strings

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserDatabaseMockImpl extends UserDatabase {

  val userDatabase = new HashMap[Int, User]
  val userByEmail = new HashMap[String, User]

  def getUserById(id: Int):Option[User] = {
    userDatabase.get(id) match {
      case Some(user) => Option.apply(user.createCopy)
      case None => None
    }
  }

  def getUserByEmail(email: String):Option[User] = {
    userByEmail.get(email) match {
      case Some(user) => Option.apply(user.createCopy)
      case None => None
    }
  }

  def addUser(user: User) {
    this.synchronized {
      if(userByEmail.get(user.email).isDefined || userDatabase.get(user.id).isDefined) {
        throw new UserAlreadyRegisteredException
      } else {
        val userCopy: User = user.createCopy
        userDatabase += user.id -> userCopy;
        userByEmail += user.email -> userCopy;
      }
    }
  }

  def updateUser(user: User) {
    this.synchronized {
      val userOption: Option[User] = userDatabase.get(user.id)
      if(userOption.isEmpty) {
        throw new IllegalStateException("No user defined in database, userId="+user.id)
      }
      
      val previousUser = userOption.get
      
      if(Strings.equal(previousUser.email, user.email)) {
        userByEmail.remove(previousUser.email)
      }

      val userCopy: User = user.createCopy
      
      userByEmail += user.email -> userCopy;
      userDatabase += user.id -> userCopy;

    }
  }
}
