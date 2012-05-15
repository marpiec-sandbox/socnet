package socnet.model

import pl.marpiec.cqrs.Aggregate
import usercontacts.{Invitation, Contact}
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */
class UserContacts extends Aggregate(null, 0) {

  var userId:UID = _

  var contacts = List[Contact]()
  var invitationsSent = List[Invitation]()
  var invitationsReceived = List[Invitation]()

  def invitationsSentById(uid:UID):Option[Invitation] = invitationsSent.find(inv => inv.id == uid)
  def invitationsReceivedById(uid:UID):Option[Invitation] = invitationsReceived.find(inv => inv.id == uid)

  def invitationsSentByUserId(uid:UID):Option[Invitation] = invitationsSent.find(inv => inv.possibleContactUserId == uid)
  def invitationsReceivedByUserId(uid:UID):Option[Invitation] = invitationsReceived.find(inv => inv.possibleContactUserId == uid)

  def contactByUserId(uid: UID):Option[Contact] = contacts.find(contact => contact.contactUserId == uid)

  def removeInvitationSentById(uid:UID) {
    invitationsSent = invitationsSent.filterNot(inv => inv.id == uid)
  }

  def removeInvitationReceivedById(uid:UID) {
    invitationsReceived = invitationsReceived.filterNot(inv => inv.id == uid)
  }

  def invitationById(uid:UID):Option[Invitation] = {
    val invitationSentOption = invitationsSentById(uid)
    if(invitationSentOption.isDefined) {
      invitationSentOption
    } else {
      val invitationReceivedOption = invitationsReceivedById(uid)
      if(invitationReceivedOption.isDefined) {
        invitationReceivedOption
      } else {
        None
      }
    }
  }

  def copy = {
    val userContacts = new UserContacts
    userContacts.id = this.id
    userContacts.version = this.version
    userContacts.userId = userId
    userContacts.contacts = contacts
    userContacts.invitationsSent = invitationsSent
    userContacts.invitationsReceived = invitationsReceived
    userContacts
  }
}
