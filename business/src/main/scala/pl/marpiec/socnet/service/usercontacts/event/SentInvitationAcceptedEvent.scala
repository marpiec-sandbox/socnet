package pl.marpiec.socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.service.exception.InvitationNotExistsException
import pl.marpiec.socnet.model.usercontacts.{Invitation, Contact}


/**
 * @author Marcin Pieciukiewicz
 */

class SentInvitationAcceptedEvent(val invitationId: UID) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
    val invitationOption: Option[Invitation] = contacts.invitationSentById(invitationId)
    if (invitationOption.isDefined) {
      val invitation = invitationOption.get

      if (contacts.contactByUserId(invitation.possibleContactUserId).isEmpty) {
        val contact = new Contact(invitation.id, invitation.possibleContactUserId)
        contacts.contacts ::= contact
      }

      invitation.accepted = true

    } else {
      throw new InvitationNotExistsException
    }

  }

  def entityClass = classOf[UserContacts]
}