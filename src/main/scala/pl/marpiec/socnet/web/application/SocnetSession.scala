package pl.marpiec.socnet.web.application

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession
import org.apache.wicket.request.Request
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import pl.marpiec.util.Strings
import pl.marpiec.di.Factory
import pl.marpiec.socnet.model.User

/**
 * Session class.
 * @author Marcin Pieciukiewicz
 */

class SocnetSession(request: Request) extends AuthenticatedWebSession(request) {

  val userQuery = Factory.userQuery
  
  var userName:String = null

  override def getRoles: Roles = null

  override def authenticate(username: String, password: String):Boolean = {

    println("Trying to authenticate "+userName+" "+password)

    val userOption = userQuery.getUserByCredentials(username, password)
    
    if(userOption.isDefined) {
      initSessionData(userOption.get)
      return true
    } else {
      return false
    }
  }

  def initSessionData(user:User) {
    userName = user.name
  }

  def clearSessionData {
    userName = null
  }

  override def invalidate {
    super.invalidate
    clearSessionData
  }


}
