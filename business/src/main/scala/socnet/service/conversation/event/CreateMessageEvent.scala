package socnet.service.conversation.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.Conversation
import socnet.model.conversation.Message

/**
 * @author Marcin Pieciukiewicz
 */

class CreateMessageEvent(val userId: UID, val messageText: String, val messageId: UID) extends Event {
  def applyEvent(aggregate: Aggregate) = {
    val conversation = aggregate.asInstanceOf[Conversation]
    val message = Message.createNewMessage(messageId, messageText, userId)
    conversation.messages ::= message
  }

  def entityClass = classOf[Conversation]
}
