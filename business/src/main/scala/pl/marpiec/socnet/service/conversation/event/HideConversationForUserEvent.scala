package pl.marpiec.socnet.service.conversation.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

class HideConversationForUserEvent(val userId: UID) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val conversation = aggregate.asInstanceOf[Conversation]
    conversation.conversationHiddenForUsers += userId
  }

  def entityClass = classOf[Conversation]

}
