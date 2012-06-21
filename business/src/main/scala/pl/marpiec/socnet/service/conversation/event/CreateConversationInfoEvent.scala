package pl.marpiec.socnet.service.conversation.event

import pl.marpiec.socnet.model.ConversationInfo
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateConversationInfoEvent(userId: UID, conversationId: UID) extends Event {


  def applyEvent(aggregate: Aggregate) {
    val conversationInfo = aggregate.asInstanceOf[ConversationInfo]
    conversationInfo.userId = userId
    conversationInfo.conversationId = conversationId
  }

  def entityClass = classOf[ConversationInfo]
}
