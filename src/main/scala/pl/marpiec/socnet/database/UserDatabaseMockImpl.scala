package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.User
import collection.mutable.HashMap

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserDatabaseMockImpl extends UserDatabase {

  val userDatabase = new HashMap[Int, User]
  val userByEmail = new HashMap[String, User]

  def getUserByd(id: Int):Option[User] = {
    userDatabase.get(id)
  }

  def getUserByEmail(email: String):Option[User] = {
    userByEmail.get(email)
  }

  def addUser(user: User) {
    userDatabase += user.id -> user.createCopy;
    userByEmail += user.email -> user.createCopy;
  }
}
