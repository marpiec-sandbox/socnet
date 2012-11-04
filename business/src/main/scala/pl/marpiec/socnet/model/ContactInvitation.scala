package pl.marpiec.socnet.model

import contactinvitation.ContactInvitationStatus
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */
class ContactInvitation extends Aggregate(null, 0) {

  var senderUserId: UID = _
  var receiverUserId: UID = _
  var message: String = _

  var status: String = ContactInvitationStatus.SENT

  def isSent = status == ContactInvitationStatus.SENT
  def isCanceled = status == ContactInvitationStatus.CANCELED
  def isAccepted = status == ContactInvitationStatus.ACCEPTED
  def isDeclined = status == ContactInvitationStatus.DECLINED

  def changeStatusToCanceled {status = ContactInvitationStatus.CANCELED}
  def changeStatusToAccepted {status = ContactInvitationStatus.ACCEPTED}
  def changeStatusToDeclined { status = ContactInvitationStatus.DECLINED}

  def copy = {
    BeanUtil.copyProperties(new ContactInvitation, this)
  }
}
