package pl.marpiec.socnet.web.application

import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.Session
import org.apache.wicket.request.{Response, Request}
import pl.marpiec.socnet.web.page.HomePage
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy

class SocnetApplication extends WebApplication {

  override def getHomePage = classOf[HomePage];

  override def init {

    SocnetBookmakablePages(this)

    getSecuritySettings.setAuthenticationStrategy(new NoOpAuthenticationStrategy)

    getSecuritySettings.setAuthorizationStrategy(new RoleAuthorizationStrategy(new UserRolesAuthorizer()));

    SocnetInitializator()

    getDebugSettings().setDevelopmentUtilitiesEnabled(true);

  }

  override def newSession(request: Request, response: Response): Session = {
    new SocnetSession(request)
  }

}
