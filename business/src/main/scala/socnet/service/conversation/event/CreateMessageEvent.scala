package socnet.service.conversation.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import socnet.model.Conversation
import socnet.model.conversation.Message
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class CreateMessageEvent(val userId: UID, val messageText: String, val sentTime:LocalDateTime, val messageId: UID) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val conversation = aggregate.asInstanceOf[Conversation]
    val message = new Message(messageId, messageText, sentTime, userId)
    conversation.messages ::= message
  }

  def entityClass = classOf[Conversation]
}
