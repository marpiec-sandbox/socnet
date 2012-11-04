package pl.marpiec.socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.UserContacts

/**
 * @author Marcin Pieciukiewicz
 */
class ContactRemovedEvent(contactUserId: UID) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
    contacts.contactsIds -= contactUserId
  }

  def entityClass = classOf[UserContacts]
}
