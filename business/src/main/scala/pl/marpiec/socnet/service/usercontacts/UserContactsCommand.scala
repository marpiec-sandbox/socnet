package pl.marpiec.socnet.service.usercontacts

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait UserContactsCommand {
  def createUserContacts(userId: UID, userAggregateId: UID, newUserContactId: UID)

  def sendInvitation(userId: UID, id: UID, invitedUserId: UID, message: String, invitationId: UID)

  def acceptInvitation(userId: UID, id: UID, invitationSenderUserId: UID, invitationId: UID)

  def declineInvitation(userId: UID, id: UID, invitationSenderUserId: UID, invitationId: UID)

  def removeSentInvitation(userId: UID, id: UID, invitationId: UID)

  def removeReceivedInvitation(userId: UID, id: UID, invitationId: UID)

}
