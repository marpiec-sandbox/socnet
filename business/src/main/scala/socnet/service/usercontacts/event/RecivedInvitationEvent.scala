package socnet.service.usercontacts.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.UserContacts

/**
 * @author Marcin Pieciukiewicz
 */

class RecivedInvitationEvent(invitationSenderUserId:UID, message:String, val invitationId:UID) extends Event {
  def applyEvent(aggregate: Aggregate) {

  }

  def entityClass = classOf[UserContacts]
}
