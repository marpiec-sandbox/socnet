package pl.marpiec.socnet.service.useractionsinfo

import event.{ChangeContactInvitationsReadTimeEvent, CreateUserActionsInfoEvent}
import pl.marpiec.util.UID
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.{EventStore, EventRow}
import org.springframework.stereotype.Service

/**
 * @author Marcin Pieciukiewicz
 */

@Service("userActionsInfoCommand")
class UserActionsInfoCommandImpl @Autowired()(val eventStore: EventStore) extends UserActionsInfoCommand {

  def createUserActionsInfo(userId: UID, actionsOwnerUserId: UID, newUserActionsInfoId: UID) = {
    val createUserActionsInfo = new CreateUserActionsInfoEvent(actionsOwnerUserId)
    eventStore.addEventForNewAggregate(newUserActionsInfoId, new EventRow(userId, newUserActionsInfoId, 0, createUserActionsInfo))
  }

  def changeContactInvitationsReadTime(userId: UID, id: UID, readTime: LocalDateTime) = {
    eventStore.addEventIgnoreVersion(new EventRow(userId, id, 0, new ChangeContactInvitationsReadTimeEvent(readTime)))
  }
}
