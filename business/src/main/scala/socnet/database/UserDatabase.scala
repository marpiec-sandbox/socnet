package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserDatabase {

  def addUser(user:User)

  def updateUser(user: User)

  def getUserByEmail(email:String):Option[User]

  def getUserById(id: UID):Option[User]
}
