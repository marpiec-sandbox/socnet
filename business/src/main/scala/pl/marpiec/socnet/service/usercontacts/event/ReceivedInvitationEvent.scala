package pl.marpiec.socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.model.usercontacts.Invitation

/**
 * @author Marcin Pieciukiewicz
 */

class ReceivedInvitationEvent(invitationSenderUserId: UID, message: String, val invitationId: UID) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
    val invitation = new Invitation(invitationId, invitationSenderUserId, message)
    contacts.invitationsReceived ::= invitation
  }

  def entityClass = classOf[UserContacts]
}
