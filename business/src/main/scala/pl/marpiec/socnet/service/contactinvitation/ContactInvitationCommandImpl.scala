package pl.marpiec.socnet.service.contactinvitation

import event.{InvitationDeclinedEvent, InvitationAcceptedEvent, InvitationCanceledEvent, SendInvitationEvent}
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.{EventRow, EventStore}
import pl.marpiec.util.UID
import pl.marpiec.socnet.service.usercontacts.UserContactsCommand
import org.springframework.stereotype.Service
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */
@Service("contactInvitationCommand")
class ContactInvitationCommandImpl @Autowired()(val eventStore: EventStore, val userContactsCommand: UserContactsCommand) extends ContactInvitationCommand {
  def sendInvitation(userId: UID, receiverUserId: UID, message: String, newInvitationId: UID) {
    val createUserContacts = new SendInvitationEvent(userId, receiverUserId, message, new LocalDateTime)
    eventStore.addEventForNewAggregate(newInvitationId, new EventRow(userId, newInvitationId, 0, createUserContacts))
  }

  def cancelInvitation(userId: UID, id: UID, version: Int) {
    eventStore.addEvent(new EventRow(userId, id, version, new InvitationCanceledEvent()))
  }

  def acceptInvitation(userId: UID, id: UID, version: Int, senderUserId: UID, receiverUserId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new InvitationAcceptedEvent()))

    userContactsCommand.markUsersAsContacts(userId, senderUserId, receiverUserId)
  }

  def declineInvitation(userId: UID, id: UID, version: Int) {
    eventStore.addEvent(new EventRow(userId, id, version, new InvitationDeclinedEvent()))
  }
}
