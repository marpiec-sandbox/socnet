package pl.marpiec.socnet.service.user

import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserCommand {

  def registerUser(name:String, email:String, password:String):UID

  def changeUserEmail(userId:UID, aggregateUserId: UID, version: Int, email: String)

}
