package socnet.service.userContact

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
class UserContactCommandImpl @Autowired() (val eventStore: EventStore) extends UserContactCommand {

  def createUserContacts(userId:UID, userAggregateId: UID, newUserContactsId:UID) {
    val createUserProfile = new CreateUserContactsEvent(userAggregateId)
    eventStore.addEventForNewAggregate(newUserContactsId, new EventRow(userId, newUserContactsId, 0, createUserProfile))
  }

  def addContact(userId: UID, id:UID, version:Int, contactUserId: UID, contactId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new AddContactEvent(contactUserId, contactId)))
  }
}
