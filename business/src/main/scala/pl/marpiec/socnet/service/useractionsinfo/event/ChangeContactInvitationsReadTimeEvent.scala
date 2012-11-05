package pl.marpiec.socnet.service.useractionsinfo.event

import org.joda.time.LocalDateTime
import pl.marpiec.socnet.model.UserActionsInfo
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * @author Marcin Pieciukiewicz
 */

class ChangeContactInvitationsReadTimeEvent(val readTime:LocalDateTime) extends Event {
  def entityClass = classOf[UserActionsInfo]

  def applyEvent(aggregate: Aggregate) {
    val userActionsInfo = aggregate.asInstanceOf[UserActionsInfo]
    userActionsInfo.contactInvitationsReadTimeOption = Option(readTime)
  }
}
