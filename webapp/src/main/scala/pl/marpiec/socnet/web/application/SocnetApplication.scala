package pl.marpiec.socnet.web.application

import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.Session
import org.apache.wicket.request.{Response, Request}
import pl.marpiec.socnet.web.page.HomePage
import org.apache.wicket.authentication.strategy.NoOpAuthenticationStrategy
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy
import org.apache.wicket.spring.injection.annot.SpringComponentInjector
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs._
import pl.marpiec.socnet.service.user.UserQuery
import org.springframework.web.context.support.WebApplicationContextUtils

class SocnetApplication extends WebApplication {

  override def getHomePage = classOf[HomePage];

  @Autowired
  var eventStore:EventStore = _

  @Autowired
  var databaseInitializer:DatabaseInitializer = _

  @Autowired
  var userQuery:UserQuery = _

  override def init {

    val webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext);

    val autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();

    autowireCapableBeanFactory.autowireBean(this);


    getComponentInstantiationListeners.add(new SpringComponentInjector(this, webApplicationContext, false))

    SocnetBookmakablePages(this)

    getSecuritySettings.setAuthenticationStrategy(new NoOpAuthenticationStrategy)

    getSecuritySettings.setAuthorizationStrategy(new RoleAuthorizationStrategy(new UserRolesAuthorizer()));

    SocnetInitializator(eventStore, databaseInitializer)

    getDebugSettings().setDevelopmentUtilitiesEnabled(true);

  }


  override def newSession(request: Request, response: Response): Session = {
    new SocnetSession(request, userQuery)
  }

}
