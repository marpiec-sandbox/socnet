package socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.UserContacts

/**
 * @author Marcin Pieciukiewicz
 */

class InvitationDeclinedEvent(val invitationId:UID) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val contacts = aggregate.asInstanceOf[UserContacts]
  }

  def entityClass = classOf[UserContacts]
}
