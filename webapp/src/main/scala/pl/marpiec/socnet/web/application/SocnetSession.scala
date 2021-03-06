package pl.marpiec.socnet.web.application

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession
import org.apache.wicket.request.Request
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy
import org.apache.commons.lang.RandomStringUtils
import pl.marpiec.util.UID
import pl.marpiec.socnet.service.user.UserQuery
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.readdatabase.UserRolesDatabase

/**
 * Session class.
 * @author Marcin Pieciukiewicz
 */

class SocnetSession(request: Request, val userQuery: UserQuery, val userRolesDatabase: UserRolesDatabase) extends AuthenticatedWebSession(request) {

  val SESSION_TOKEN_LENGTH = 32

  private var roles: Roles = initDefaultRoles
  var sessionToken: String = _
  var user: User = null

  private def initDefaultRoles: Roles = {
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
    this.sessionToken = RandomStringUtils.randomAlphanumeric(SESSION_TOKEN_LENGTH)

    val userRolesOption = userRolesDatabase.getRolesByUserId(registeredUser.id)

    roles.clear

    if (userRolesOption.isDefined) {
      userRolesOption.get.roles.foreach(role => {
        roles.add(role)
      })
    }

  }

  def clearSessionData() {
    user = null
    roles = initDefaultRoles
  }

  override def invalidate {
    super.invalidate()
    clearSessionData
  }

  def isAuthenticated(): Boolean = {
    user != null
  }

  def userId: UID = {
    if (isAuthenticated()) {
      user.id
    } else {
      null
    }
  }


}
