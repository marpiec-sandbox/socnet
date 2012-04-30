package pl.marpiec.socnet.web.application

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession
import org.apache.wicket.request.Request
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.di.Factory
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy
import org.apache.commons.lang.RandomStringUtils
import pl.marpiec.util.UID

/**
 * Session class.
 * @author Marcin Pieciukiewicz
 */

class SocnetSession(request: Request) extends AuthenticatedWebSession(request) {

  val SESSION_TOKEN_LENGTH = 32
  
  private val userQuery = Factory.userQuery
  
  private var roles: Roles = initDefaultRoles
  var sessionToken:String = _
  var user: User = null
  
  private def initDefaultRoles:Roles = {
    val r = new Roles
    r.add(MetaDataRoleAuthorizationStrategy.NO_ROLE)
    r
  }

  override def getRoles: Roles = roles

  override def authenticate(login: String, password: String): Boolean = {

    val userOption = userQuery.getUserByCredentials(login, password)

    if (userOption.isDefined) {
      initSessionData(userOption.get)
      true
    } else {
      false
    }
  }

  private def initSessionData(registeredUser: User) {
    
    this.user = registeredUser
    
    this.sessionToken = RandomStringUtils.randomAlphabetic(SESSION_TOKEN_LENGTH)

    roles.clear
    roles.add(SocnetRoles.USER)
    roles.add(SocnetRoles.ARTICLE_AUTHOR)

  }

  def clearSessionData() {
    user = null
    roles = initDefaultRoles
  }

  override def invalidate {
    super.invalidate()
    clearSessionData
  }

  def isAuthenticated():Boolean = {
    user != null
  }
  
  def userId():UID = {
    if (isAuthenticated()) {
      user.id
    } else {
      null
    }
  }


}
