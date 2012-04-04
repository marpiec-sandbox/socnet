package pl.marpiec.socnet.service.user

import pl.marpiec.socnet.model.User

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserQuery {

  def getUserById(id:Int):User

  def getUserByCredentials(username:String, password:String):User
}
