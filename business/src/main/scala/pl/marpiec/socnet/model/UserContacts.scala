package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import usercontacts.{Invitation, Contact}
import pl.marpiec.util.{BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */
class UserContacts extends Aggregate(null, 0) {

  var userId: UID = _

  var contacts = List[Contact]()
  var invitationsSent = List[Invitation]()
  var invitationsReceived = List[Invitation]()

  def invitationSentById(uid: UID): Option[Invitation] = invitationsSent.find(inv => inv.id == uid)

  def invitationReceivedById(uid: UID): Option[Invitation] = invitationsReceived.find(inv => inv.id == uid)

  def invitationSentByUserId(uid: UID): Option[Invitation] = invitationsSent.find(inv => inv.possibleContactUserId == uid)

  def invitationReceivedByUserId(uid: UID): Option[Invitation] = invitationsReceived.find(inv => inv.possibleContactUserId == uid)

  def contactByUserId(uid: UID): Option[Contact] = contacts.find(contact => contact.contactUserId == uid)

  def notRemovedInvitationsSent: List[Invitation] = invitationsSent.filterNot(inv => inv.removed)

  def notRemovedInvitationsReceived: List[Invitation] = invitationsReceived.filterNot(inv => inv.removed)


  def copy = {
    BeanUtil.copyProperties(new UserContacts, this)
  }
}
