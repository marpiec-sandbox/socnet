package socnet.service.usercontacts

import event.{InvitationDeclinedEvent, InvitationAcceptedEvent, SendInvitationEvent}
import pl.marpiec.util.UID
import pl.marpiec.cqrs.EventRow

/**
 * @author Marcin Pieciukiewicz
 */

trait UserContactsCommand {
  def createUserContacts(userId:UID, userAggregateId: UID, newUserContactId:UID)

  def sendInvitation(userId:UID, id:UID, version:Int, invitedUserId: UID, message:String, invitationId:UID)

  def acceptInvitation(userId:UID, id:UID, version:Int, invitationSenderUserId: UID, invitationId:UID)

  def declineInvitation(userId:UID, id:UID, version:Int, invitationSenderUserId: UID, invitationId:UID)
}
