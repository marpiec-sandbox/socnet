package socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.UserContacts
import socnet.service.exception.InvitationNotExistsException
import socnet.model.usercontacts.Contact

/**
 * @author Marcin Pieciukiewicz
 */

class RecivedInvitationDeclinedEvent(val invitationId: UID) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
    val invitationOption = contacts.invitationsReceivedById(invitationId)
    if (invitationOption.isDefined) {
      contacts.removeInvitationReceivedById(invitationId)
    } else {
      throw new InvitationNotExistsException
    }

  }

  def entityClass = classOf[UserContacts]
}
