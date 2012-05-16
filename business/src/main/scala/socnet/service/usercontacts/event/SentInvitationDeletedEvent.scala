package socnet.service.usercontacts.event

import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.UserContacts
import socnet.service.exception.InvitationNotExistsException
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class SentInvitationDeletedEvent(val invitationId: UID) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
    val invitationOption = contacts.invitationsSentById(invitationId)
    if (invitationOption.isDefined) {
      invitationOption.get.removed = true
    } else {
      throw new InvitationNotExistsException
    }
  }

  def entityClass = classOf[UserContacts]
}