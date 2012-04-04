package pl.marpiec.socnet.web.application

import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.Session
import org.apache.wicket.request.{Response, Request}
import pl.marpiec.socnet.web.page.{SignOutPage, RegisterPage, HomePage}
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy

class SocnetApplication extends WebApplication {


  override def getHomePage = classOf[HomePage];

  override def init {
    mountPage("register", classOf[RegisterPage])
    mountPage("signout", classOf[SignOutPage])

    getSecuritySettings.setAuthenticationStrategy(new NoOpAuthenticationStrategy)

  }

  override def newSession(request: Request, response: Response): Session = {
    new SocnetSession(request)
  }


}
