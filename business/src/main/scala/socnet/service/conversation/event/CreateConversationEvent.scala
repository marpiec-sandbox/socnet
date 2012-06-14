package socnet.service.conversation.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.Conversation
import socnet.model.conversation.Message

/**
 * @author Marcin Pieciukiewicz
 */

class CreateConversationEvent(val creatorUserId: UID, val title: String, val participantsUserIds: Array[UID],
                              val firstMessageText: String, val firstMessageId: UID) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val conversation = aggregate.asInstanceOf[Conversation]
    conversation.creatorUserId = creatorUserId;
    conversation.title = title
    conversation.participantsUserIds = participantsUserIds.toList

    val message = Message.createNewMessage(firstMessageId, firstMessageText, creatorUserId)
    conversation.messages ::= message
  }

  def entityClass = classOf[Conversation]
}
