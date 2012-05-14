package socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.UserContacts
import socnet.service.exception.InvitationNotExistsException
import socnet.model.usercontacts.Contact


/**
 * @author Marcin Pieciukiewicz
 */

class InvitationAcceptedEvent(val invitationId:UID) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
    val invitationOption = contacts.invitationById(invitationId)
    if(invitationOption.isDefined) {
      val invitation = invitationOption.get

      //TODO zmienic id na nowy id dla kontaktu
      val contact = new Contact(invitation.id, invitation.possibleContactUserId)
      contacts.contacts ::= contact

      contacts.removeInvitationSentById(invitationId)
      contacts.removeInvitationReceivedById(invitationId)

    } else {
      throw new InvitationNotExistsException
    }

  }

  def entityClass = classOf[UserContacts]
}
