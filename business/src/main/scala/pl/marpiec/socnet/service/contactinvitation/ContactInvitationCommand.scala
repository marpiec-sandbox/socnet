package pl.marpiec.socnet.service.contactinvitation

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */
trait ContactInvitationCommand {
  def sendInvitation(userId: UID, receiverUserId: UID, message: String, newInvitationId: UID)

  def cancelInvitation(userId: UID, id: UID, version: Int)

  def acceptInvitation(userId: UID, id: UID, version: Int, senderUserId: UID, receiverUserId: UID)

  def declineInvitation(userId: UID, id: UID, version: Int)

}
