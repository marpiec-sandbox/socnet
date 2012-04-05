package pl.marpiec.socnet.web.application

import org.apache.wicket.authroles.authorization.strategies.role.{Roles, IRoleCheckingStrategy}
import org.apache.wicket.Session


/**
 * @author Marcin Pieciukiewicz
 */

class UserRolesAuthorizer extends IRoleCheckingStrategy {
  def hasAnyRole(roles: Roles) = {
    val session: SocnetSession = Session.get.asInstanceOf[SocnetSession]
    session.getRoles.hasAnyRole(roles)
  }
}
