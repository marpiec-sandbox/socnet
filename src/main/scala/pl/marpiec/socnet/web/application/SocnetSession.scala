package pl.marpiec.socnet.web.application

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession
import org.apache.wicket.request.Request
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import pl.marpiec.di.Factory
import pl.marpiec.socnet.model.User

/**
 * Session class.
 * @author Marcin Pieciukiewicz
 */

class SocnetSession(request: Request) extends AuthenticatedWebSession(request) {

  private val userQuery = Factory.userQuery

  private val roles: Roles = new Roles

  var user: User = null


  override def getRoles: Roles = roles

  override def authenticate(login: String, password: String): Boolean = {

    val userOption = userQuery.getUserByCredentials(login, password)

    if (userOption.isDefined) {
      initSessionData(userOption.get)
      return true
    } else {
      return false
    }
  }

  def initSessionData(registeredUser: User) {
    this.user = registeredUser

    roles.add(SocnetRoles.USER)
    roles.add(SocnetRoles.ARTICLE_AUTHOR)

  }

  def clearSessionData {
    user = null
    roles.clear()
  }

  override def invalidate {
    super.invalidate
    clearSessionData
  }


}
