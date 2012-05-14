package socnet.service.usercontacts

import event.{CreateUserContactsEvent, AddContactEvent}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{EventStore, EventRow}
import pl.marpiec.socnet.service.userprofile.event.{CreateUserProfileEvent, AddJobExperienceEvent}

/**
 * @author Marcin Pieciukiewicz
 */

@Service("userContactCommand")
class UserContactsCommandImpl @Autowired() (val eventStore: EventStore) extends UserContactsCommand {

  def createUserContacts(userId:UID, userAggregateId: UID, newUserContactId:UID) {
    val createUserContacts = new CreateUserContactsEvent(userAggregateId)
    eventStore.addEventForNewAggregate(newUserContactId, new EventRow(userId, newUserContactId, 0, createUserContacts))
  }

  /**
   * Adds contact for user
   * @param userId user who executed command
   * @param id id of userContacts aggregate
   * @param version version of userContacts aggregate
   * @param contactUserId id of user added to contacts
   * @param contactId id of new contact entry
   */
  def addContact(userId: UID, id:UID, version:Int, contactUserId: UID, contactId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new AddContactEvent(contactUserId, contactId)))
  }
}
