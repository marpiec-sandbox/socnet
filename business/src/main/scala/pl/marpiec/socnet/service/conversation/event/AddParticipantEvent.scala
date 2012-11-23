package pl.marpiec.socnet.service.conversation.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

class AddParticipantEvent(val addedParticipantUserId: UID) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val conversation = aggregate.asInstanceOf[Conversation]
    conversation.invitedUserIds ::= addedParticipantUserId
    conversation.previousUserIds = conversation.previousUserIds.filterNot(_ == addedParticipantUserId)
  }

  def entityClass = classOf[Conversation]
}
