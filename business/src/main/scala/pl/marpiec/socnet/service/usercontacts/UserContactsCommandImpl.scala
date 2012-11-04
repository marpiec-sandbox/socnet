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

  def markUsersAsContacts(userId: UID, invitationSenderUserId: UID, invitationReceiverUserId: UID) {

    val senderContacts = userContactsDatabase.getUserContactsByUserId(invitationSenderUserId)
      .getOrElse(throw new UserContactsNotExistsException)

    val receiverContacts = userContactsDatabase.getUserContactsByUserId(invitationReceiverUserId)
      .getOrElse(throw new UserContactsNotExistsException)

    eventStore.addEventIgnoreVersion(new EventRow(userId, senderContacts.id, senderContacts.version, new ContactCreatedEvent(invitationReceiverUserId)))
    eventStore.addEventIgnoreVersion(new EventRow(userId, receiverContacts.id, receiverContacts.version, new ContactCreatedEvent(invitationSenderUserId)))

  }

  def removeContact(userId: UID, firstUserId: UID, secondUserId: UID) {

    val firstUserContacts = userContactsDatabase.getUserContactsByUserId(firstUserId)
      .getOrElse(throw new UserContactsNotExistsException)

    val secondUserContacts = userContactsDatabase.getUserContactsByUserId(secondUserId)
      .getOrElse(throw new UserContactsNotExistsException)

    eventStore.addEventIgnoreVersion(new EventRow(userId, firstUserContacts.id, firstUserContacts.version, new ContactRemovedEvent(secondUserId)))
    eventStore.addEventIgnoreVersion(new EventRow(userId, secondUserContacts.id, secondUserContacts.version, new ContactRemovedEvent(firstUserId)))

  }
}
