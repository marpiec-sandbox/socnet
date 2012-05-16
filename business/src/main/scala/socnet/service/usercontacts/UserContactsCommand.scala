package socnet.service.usercontacts

import event.{SentInvitationDeclinedEvent, SentInvitationAcceptedEvent, SendInvitationEvent}
import pl.marpiec.util.UID
import pl.marpiec.cqrs.EventRow

/**
 * @author Marcin Pieciukiewicz
 */

trait UserContactsCommand {
  def createUserContacts(userId:UID, userAggregateId: UID, newUserContactId:UID)

  def sendInvitation(userId:UID, id:UID, invitedUserId: UID, message:String, invitationId:UID)

  def acceptInvitation(userId:UID, id:UID, invitationSenderUserId: UID, invitationId:UID)

  def declineInvitation(userId:UID, id:UID, invitationSenderUserId: UID, invitationId:UID)
}
