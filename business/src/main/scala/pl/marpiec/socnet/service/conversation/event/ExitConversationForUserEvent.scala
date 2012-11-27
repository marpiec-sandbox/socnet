package pl.marpiec.socnet.service.conversation.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

class ExitConversationForUserEvent(val userId: UID) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val conversation = aggregate.asInstanceOf[Conversation]

    conversation.invitedUserIds += userId
    conversation.participantsUserIds = conversation.participantsUserIds.filterNot(_ == userId)
  }

  def entityClass = classOf[Conversation]

}

