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

  def getUserByd(id: Int):User = {
    userDatabase.get(id).getOrElse(null)
  }

  def getUserByEmail(email: String):User = {
    userByEmail.get(email).getOrElse(null)
  }

  def addUser(user: User) {
    userDatabase += user.id -> user.createCopy;
    userByEmail += user.email -> user.createCopy;
  }
}
