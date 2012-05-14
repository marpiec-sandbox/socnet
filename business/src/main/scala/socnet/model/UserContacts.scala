package socnet.model

import pl.marpiec.cqrs.Aggregate
import usercontacts.Contact

/**
 * @author Marcin Pieciukiewicz
 */

class UserContacts extends Aggregate(null, 0) {

  var contacts = List[Contact]()

  def copy = {
    val copy = new UserContacts
    copy.contacts = contacts
    copy
  }
}
