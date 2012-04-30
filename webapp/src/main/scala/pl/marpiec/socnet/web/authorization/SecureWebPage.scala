package pl.marpiec.socnet.web.authorization

import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SecureWebPage(roles: Array[String]) extends SimpleTemplatePage {

  def this(role: String) {
    this(Array(role))
  }


  if (anyRolesRequired && haveNoneOfRoles) {
    throw new AbortWithHttpErrorCodeException(403)
  }


  // methods

  private def anyRolesRequired: Boolean = {
    roles.size > 0
  }

  private def haveNoneOfRoles: Boolean = {
    !getSession.asInstanceOf[SocnetSession].getRoles.hasAnyRole(new Roles(roles))
  }
}
