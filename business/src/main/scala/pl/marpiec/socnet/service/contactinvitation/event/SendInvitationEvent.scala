package pl.marpiec.socnet.service.contactinvitation.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.ContactInvitation

/**
 * @author Marcin Pieciukiewicz
 */

class SendInvitationEvent(val senderUserId: UID, val receiverUserId: UID, val message: String) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val invitation = aggregate.asInstanceOf[ContactInvitation]
    invitation.senderUserId = senderUserId
    invitation.receiverUserId = receiverUserId
    invitation.message = message
  }

  def entityClass = classOf[ContactInvitation]
}
