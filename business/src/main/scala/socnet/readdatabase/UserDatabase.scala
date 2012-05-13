package pl.marpiec.socnet.readdatabase

import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserDatabase {
  def findUser(query: String):List[User]

  def addUser(user:User)

  def updateUser(user: User)

  def getUserByEmail(email:String):Option[User]

  def getUserById(id: UID):Option[User]
}
