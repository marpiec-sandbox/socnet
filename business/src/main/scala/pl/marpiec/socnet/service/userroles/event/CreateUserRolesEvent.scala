package pl.marpiec.socnet.service.userroles.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.UserRoles
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateUserRolesEvent(val userId: UID, val roles: Set[String]) extends Event {
  def entityClass = classOf[UserRoles]

  def applyEvent(aggregate: Aggregate) {
    val userRoles = aggregate.asInstanceOf[UserRoles]

    userRoles.userId = userId

    roles.foreach(role => {
      userRoles.roles += role
    })
  }
}
