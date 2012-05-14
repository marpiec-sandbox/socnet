package socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.UserContacts
import socnet.model.usercontacts.Contact
import socnet.service.exception.InvitationNotExistsException

/**
 * @author Marcin Pieciukiewicz
 */

class SentInvitationDeclinedEvent(val invitationId:UID) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
    val invitationOption = contacts.invitationsReceivedById(invitationId)
    if (invitationOption.isDefined) {
      val invitation = invitationOption.get
      invitation.declined = true
    } else {
      throw new InvitationNotExistsException
    }

  }

  def entityClass = classOf[UserContacts]
}
