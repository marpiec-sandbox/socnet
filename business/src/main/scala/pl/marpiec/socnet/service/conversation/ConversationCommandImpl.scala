package pl.marpiec.socnet.service.conversation

import event._
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.util.UID
import collection.immutable.List
import org.springframework.stereotype.Service
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.{UidGenerator, EventRow, EventStore}

/**
 * @author Marcin Pieciukiewicz
 */

@Service("conversationCommand")
class ConversationCommandImpl @Autowired()(val eventStore: EventStore, val uidGenerator: UidGenerator) extends ConversationCommand {


  def createConversation(userId: UID, title: String, participantsUserIds: List[UID], newConversationId: UID,
                         firstMessageText: String, firstMessageId: UID) {
    val createConversation = new CreateConversationEvent(userId, title, participantsUserIds.toArray, new LocalDateTime(), firstMessageText, firstMessageId)
    eventStore.addEventForNewAggregate(newConversationId, new EventRow(userId, newConversationId, 0, createConversation))
  }

  def createMessage(userId: UID, id: UID, version: Int, messageText: String, messageId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new CreateMessageEvent(userId, messageText, new LocalDateTime(), messageId)))
  }

  def addParticipant(userId: UID, id: UID, version: Int, message: String, addedParticipantUserId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new AddParticipantEvent(userId, message, addedParticipantUserId)))
  }

  def hideConversation(userId: UID, id: UID, version: Int) {
    eventStore.addEvent(new EventRow(userId, id, version, new HideConversationForUserEvent(userId)))
  }

  def userHasReadConversation(userId: UID, conversationInfoIdAndVersionOption: Option[(UID, Int)], conversationId: UID) {
    val (conversationInfoId, version) = addConversationInfoForUserIfRequired(conversationInfoIdAndVersionOption, userId, conversationId);
    eventStore.addEvent(new EventRow(userId, conversationInfoId, version, new UserHasReadConversationEvent(userId, new LocalDateTime())))
  }

  private def addConversationInfoForUserIfRequired(conversationInfoIdAndVersionOption: Option[(UID, Int)],
                                                   userId: UID, conversationId: UID): (UID, Int) = {
    if (conversationInfoIdAndVersionOption.isEmpty) {
      val createConversationInfo = new CreateConversationInfoEvent(userId, conversationId)
      val conversationInfoId = uidGenerator.nextUid

      eventStore.addEventForNewAggregate(conversationInfoId, new EventRow(userId, conversationInfoId, 0, createConversationInfo))
      (conversationInfoId, 1)
    } else {
      conversationInfoIdAndVersionOption.get
    }
  }
}
