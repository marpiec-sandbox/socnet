package socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.UserContacts
import socnet.model.usercontacts.Contact

/**
 * @author Marcin Pieciukiewicz
 */

class AddContactEvent(val contactUserId: UID, val contactId: UID) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val userContacts = aggregate.asInstanceOf[UserContacts]
    val newContact = new Contact(contactId, contactUserId)
    userContacts.contacts = newContact :: userContacts.contacts
  }

  def entityClass = classOf[UserContacts]
}
