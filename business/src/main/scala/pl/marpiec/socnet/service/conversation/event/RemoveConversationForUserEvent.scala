package pl.marpiec.socnet.service.conversation.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.ConversationInfo

/**
 * @author Marcin Pieciukiewicz
 */

class RemoveConversationForUserEvent() extends Event {

  def applyEvent(aggregate: Aggregate) {
    val conversationInfo = aggregate.asInstanceOf[ConversationInfo]
    conversationInfo.deleted = true
    conversationInfo.participating = false
  }

  def entityClass = classOf[ConversationInfo]

}
