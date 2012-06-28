package pl.marpiec.socnet.service.conversation.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.ConversationInfo

/**
 * @author Marcin Pieciukiewicz
 */

class ExitConversationForUserEvent() extends Event {

  def applyEvent(aggregate: Aggregate) {
    val conversationInfo = aggregate.asInstanceOf[ConversationInfo]
    conversationInfo.participating = false
  }

  def entityClass = classOf[ConversationInfo]

}

