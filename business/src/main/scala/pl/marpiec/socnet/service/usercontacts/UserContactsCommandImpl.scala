package pl.marpiec.socnet.service.usercontacts

import event._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{EventStore, EventRow}
import pl.marpiec.socnet.readdatabase.UserContactsDatabase
import pl.marpiec.socnet.service.exception.UserContactsNotExistsException

/**
 * @author Marcin Pieciukiewicz
 */

@Service("userContactCommand")
class UserContactsCommandImpl @Autowired()(val eventStore: EventStore, val userContactsDatabase: UserContactsDatabase) extends UserContactsCommand {

  def createUserContacts(userId: UID, userAggregateId: UID, newUserContactId: UID) {
    val createUserContacts = new CreateUserContactsEvent(userAggregateId)
    eventStore.addEventForNewAggregate(newUserContactId, new EventRow(userId, newUserContactId, 0, createUserContacts))
  }

  def sendInvitation(userId: UID, id: UID, invitedUserId: UID, message: String, invitationId: UID) {

    //sender
    eventStore.addEventIgnoreVersion(new EventRow(userId, id, 0, new SendInvitationEvent(invitedUserId, message, invitationId)))

    //receiver
    val contactContactsIdOption = userContactsDatabase.getUserContactsIdByUserId(invitedUserId)

    if (contactContactsIdOption.isEmpty) {
      throw new UserContactsNotExistsException
    }

    eventStore.addEventIgnoreVersion(new EventRow(userId, contactContactsIdOption.get, 0, new ReceivedInvitationEvent(userId, message, invitationId)))
  }

  def acceptInvitation(userId: UID, id: UID, invitationSenderUserId: UID, invitationId: UID) {
    eventStore.addEventIgnoreVersion(new EventRow(userId, id, 0, new ReceivedInvitationAcceptedEvent(invitationId)))

    val senderContactsIdOption = userContactsDatabase.getUserContactsIdByUserId(invitationSenderUserId)
    if (senderContactsIdOption.isEmpty) {
      throw new UserContactsNotExistsException
    }

    eventStore.addEventIgnoreVersion(new EventRow(userId, senderContactsIdOption.get, 0, new SentInvitationAcceptedEvent(invitationId)))
  }

  def declineInvitation(userId: UID, id: UID, invitationSenderUserId: UID, invitationId: UID) {
    eventStore.addEventIgnoreVersion(new EventRow(userId, id, 0, new ReceivedInvitationDeclinedEvent(invitationId)))

    val senderContactsIdOption = userContactsDatabase.getUserContactsIdByUserId(invitationSenderUserId)
    if (senderContactsIdOption.isEmpty) {
      throw new UserContactsNotExistsException
    }

    eventStore.addEventIgnoreVersion(new EventRow(userId, senderContactsIdOption.get, 0, new SentInvitationDeclinedEvent(invitationId)))
  }

  def removeSentInvitation(userId: UID, id: UID, invitationId: UID) {
    eventStore.addEventIgnoreVersion(new EventRow(userId, id, 0, new SentInvitationDeletedEvent(invitationId)))
  }

  def removeReceivedInvitation(userId: UID, id: UID, invitationId: UID) {
    eventStore.addEventIgnoreVersion(new EventRow(userId, id, 0, new ReceivedInvitationDeletedEvent(invitationId)))
  }
}