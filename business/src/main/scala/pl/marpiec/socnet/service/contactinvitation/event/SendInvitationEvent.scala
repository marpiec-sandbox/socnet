package pl.marpiec.socnet.service.contactinvitation.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.ContactInvitation
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class SendInvitationEvent(val senderUserId: UID, val receiverUserId: UID,
                          val message: String, val sendTime:LocalDateTime) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val invitation = aggregate.asInstanceOf[ContactInvitation]
    invitation.senderUserId = senderUserId
    invitation.receiverUserId = receiverUserId
    invitation.message = message
    invitation.sendTime = sendTime
  }

  def entityClass = classOf[ContactInvitation]
}
