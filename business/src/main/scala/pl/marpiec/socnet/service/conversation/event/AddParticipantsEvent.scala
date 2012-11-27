package pl.marpiec.socnet.service.conversation.event

import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

class AddParticipantsEvent(val addedParticipantsUserIds: List[UID]) extends Event {
  def applyEvent(aggregate: Aggregate) {
    val conversation = aggregate.asInstanceOf[Conversation]

    addedParticipantsUserIds.foreach(userId => {
      if(!conversation.userParticipating(userId)) {
        conversation.invitedUserIds += userId
      }
      conversation.previousUserIds = conversation.previousUserIds.filterNot(_ == userId)
    })

  }

  def entityClass = classOf[Conversation]
}
