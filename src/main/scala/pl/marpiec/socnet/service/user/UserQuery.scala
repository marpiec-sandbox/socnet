package pl.marpiec.socnet.service.user

import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserQuery {

  def getUserById(id:UID):User

  def getUserByCredentials(username:String, password:String):Option[User]
}
