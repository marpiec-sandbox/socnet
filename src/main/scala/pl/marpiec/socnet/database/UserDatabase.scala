package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.User

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserDatabase {

  def addUser(user:User)

  def getUserByEmail(email:String):Option[User]

  def getUserByd(id: Int):Option[User]
}
