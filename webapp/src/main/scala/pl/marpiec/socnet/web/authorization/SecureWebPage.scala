package pl.marpiec.socnet.web.authorization

import org.apache.wicket.markup.html.WebPage
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SecureWebPage(roles:Array[String]) extends WebPage {

  def this(role:String) {
    this(Array(role))
  }

  if(!getSession.asInstanceOf[SocnetSession].getRoles.hasAnyRole(new Roles(roles))) {
    throw new AbortWithHttpErrorCodeException(403)
  }
}
