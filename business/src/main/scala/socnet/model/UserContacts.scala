package socnet.model

import pl.marpiec.cqrs.Aggregate
import usercontacts.{Invitation, Contact}

/**
 * @author Marcin Pieciukiewicz
 */
class UserContacts extends Aggregate(null, 0) {

  var contacts = List[Contact]()

  var invitations = List[Invitation]()

  def copy = {
    val copy = new UserContacts
    copy.contacts = contacts
    copy.invitations = invitations
    copy
  }
}
