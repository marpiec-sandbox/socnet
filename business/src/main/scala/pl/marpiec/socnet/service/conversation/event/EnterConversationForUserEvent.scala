package pl.marpiec.socnet.service.conversation.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.Conversation
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class EnterConversationForUserEvent(val userId: UID) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val conversation = aggregate.asInstanceOf[Conversation]

    conversation.invitedUserIds = conversation.invitedUserIds.filterNot(_ == userId)
    conversation.participantsUserIds ::= userId
  }

  def entityClass = classOf[Conversation]

}


