package pl.marpiec.socnet.service.user

import pl.marpiec.socnet.model.User

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserCommand {

  def registerUser(name:String, email:String, password:String):Int

  def changeUserEmail(id: Int, version: Int, email: String)

}
