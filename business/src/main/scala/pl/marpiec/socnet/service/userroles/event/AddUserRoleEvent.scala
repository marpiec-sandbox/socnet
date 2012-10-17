package pl.marpiec.socnet.service.userroles.event

import pl.marpiec.socnet.model.UserRoles
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * @author Marcin Pieciukiewicz
 */

class AddUserRoleEvent(val role: String) extends Event {
  def entityClass = classOf[UserRoles]

  def applyEvent(aggregate: Aggregate) {
    val userRoles = aggregate.asInstanceOf[UserRoles]
    userRoles.roles += role
  }
}
