package pl.marpiec.socnet.service.user

import pl.marpiec.socnet.model.User
import java.util.UUID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserQuery {

  def getUserById(uuid:UUID):User

  def getUserByCredentials(username:String, password:String):Option[User]
}
