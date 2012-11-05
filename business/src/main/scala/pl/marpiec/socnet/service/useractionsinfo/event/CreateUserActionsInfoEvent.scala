package pl.marpiec.socnet.service.useractionsinfo.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.UserActionsInfo

/**
 * @author Marcin Pieciukiewicz
 */

class CreateUserActionsInfoEvent(val userId: UID) extends Event {

  def entityClass = classOf[UserActionsInfo]

  def applyEvent(aggregate: Aggregate) {
    val userActionsInfo = aggregate.asInstanceOf[UserActionsInfo]
    userActionsInfo.userId = userId
  }
}
