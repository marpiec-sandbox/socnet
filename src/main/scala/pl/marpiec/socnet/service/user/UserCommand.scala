package pl.marpiec.socnet.service.user

import pl.marpiec.socnet.model.User
import java.util.UUID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserCommand {

  def registerUser(name:String, email:String, password:String):UUID

  def changeUserEmail(uuid: UUID, version: Int, email: String)

}
