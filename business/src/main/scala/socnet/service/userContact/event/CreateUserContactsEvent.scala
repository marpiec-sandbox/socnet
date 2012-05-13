package socnet.service.userContact.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.UserContacts

/**
 * @author Marcin Pieciukiewicz
 */

class CreateUserContactsEvent(userId:UID) extends Event {
  def applyEvent(aggregate: Aggregate) {}

  def entityClass = classOf[UserContacts]
}
