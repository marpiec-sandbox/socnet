package socnet.service.conversation.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

class CreateConversationEvent(creatorUserId:UID, title: String, participantsUserIds: List[UID]) extends Event {
  def applyEvent(aggregate: Aggregate) = {
    val conversation = aggregate.asInstanceOf[Conversation]
    conversation.creatorUserId = creatorUserId;
    conversation.title = title
    conversation.participantsUserIds = participantsUserIds
  }

  def entityClass = classOf[Conversation]
}
