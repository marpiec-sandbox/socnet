package pl.marpiec.socnet.service.contactinvitation.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.ContactInvitation

/**
 * @author Marcin Pieciukiewicz
 */
class InvitationDeclinedEvent extends Event {

  def applyEvent(aggregate: Aggregate) {
    val contactInvitation = aggregate.asInstanceOf[ContactInvitation]
    contactInvitation.changeStatusToDeclined
  }

  def entityClass = classOf[ContactInvitation]
}
