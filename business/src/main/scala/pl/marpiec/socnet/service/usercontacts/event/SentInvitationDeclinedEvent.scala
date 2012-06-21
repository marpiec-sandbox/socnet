package pl.marpiec.socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.service.exception.InvitationNotExistsException

/**
 * @author Marcin Pieciukiewicz
 */

class SentInvitationDeclinedEvent(val invitationId: UID) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
    val invitationOption = contacts.invitationSentById(invitationId)
    if (invitationOption.isDefined) {
      invitationOption.get.declined = true
    } else {
      throw new InvitationNotExistsException
    }

  }

  def entityClass = classOf[UserContacts]
}
