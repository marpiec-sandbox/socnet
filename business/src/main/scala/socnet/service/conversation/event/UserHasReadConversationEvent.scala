package socnet.service.conversation.event

import pl.marpiec.util.UID
import socnet.model.ConversationInfo
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * @author Marcin Pieciukiewicz
 */

class UserHasReadConversationEvent(val userId: UID, val readTime: LocalDateTime) extends Event {


  def applyEvent(aggregate: Aggregate) {
    val conversationInfo = aggregate.asInstanceOf[ConversationInfo]
    conversationInfo.lastReadTime = readTime
  }

  def entityClass = classOf[ConversationInfo]

}
