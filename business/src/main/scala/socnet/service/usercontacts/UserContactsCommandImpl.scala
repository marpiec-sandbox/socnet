package socnet.service.usercontacts

import event._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{EventStore, EventRow}

/**
 * @author Marcin Pieciukiewicz
 */

@Service("userContactCommand")
class UserContactsCommandImpl @Autowired() (val eventStore: EventStore) extends UserContactsCommand {

  def createUserContacts(userId:UID, userAggregateId: UID, newUserContactId:UID) {
    val createUserContacts = new CreateUserContactsEvent(userAggregateId)
    eventStore.addEventForNewAggregate(newUserContactId, new EventRow(userId, newUserContactId, 0, createUserContacts))
  }
  
  def sendInvitation(userId:UID, id:UID, version:Int, invitedUserId: UID, message:String, invitationId:UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new SendInvitationEvent(invitedUserId, message, invitationId)))

   // eventStore.addEvent(new EventRow(userId, id, version, new SendInvitationEvent(invitedUserId, message, invitationId)))

    //TODO add reciving message
  }

  def acceptInvitation(userId:UID, id:UID, version:Int, invitationSenderUserId: UID, invitationId:UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new InvitationAcceptedEvent(invitationId)))

    //TODO add reciving message
  }

  def declineInvitation(userId:UID, id:UID, version:Int, invitationSenderUserId: UID, invitationId:UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new InvitationDeclinedEvent(invitationId)))

    //TODO add reciving message
  }

}
