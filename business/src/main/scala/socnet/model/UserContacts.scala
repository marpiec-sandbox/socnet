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
