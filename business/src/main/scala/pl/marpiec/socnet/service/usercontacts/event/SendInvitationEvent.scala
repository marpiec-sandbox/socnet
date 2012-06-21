package pl.marpiec.socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.model.usercontacts.Invitation

/**
 * @author Marcin Pieciukiewicz
 */

class SendInvitationEvent(val invitedUserId: UID, val message: String, val invitationId: UID) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
    val invitation = new Invitation(invitationId, invitedUserId, message)
    contacts.invitationsSent ::= invitation
  }

  def entityClass = classOf[UserContacts]
}
