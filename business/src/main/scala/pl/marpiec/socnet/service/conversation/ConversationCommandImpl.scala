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
    val createConversation = new CreateConversationEvent(userId, title, participantsUserIds, new LocalDateTime(), firstMessageText, firstMessageId)
    eventStore.addEventForNewAggregate(newConversationId, new EventRow(userId, newConversationId, 0, createConversation))
    participantsUserIds.foreach(participantId => addConversationInfoForUser(participantId, newConversationId))
  }


  private def addConversationInfoForUser(userId: UID, conversationId: UID) {
    val createConversationInfo = new CreateConversationInfoEvent(userId, conversationId)
    val conversationInfoId = uidGenerator.nextUid
    eventStore.addEventForNewAggregate(conversationInfoId, new EventRow(userId, conversationInfoId, 0, createConversationInfo))
  }

  def createMessage(userId: UID, id: UID, version: Int, messageText: String, messageId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new CreateMessageEvent(userId, messageText, new LocalDateTime(), messageId)))
  }

  def addParticipant(userId: UID, id: UID, version: Int, message: String, addedParticipantUserId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new AddParticipantEvent(userId, message, addedParticipantUserId)))
  }

  def userHasReadConversation(userId: UID, conversationInfoId: UID, version: Int) {
    eventStore.addEvent(new EventRow(userId, conversationInfoId, version, new UserHasReadConversationEvent(new LocalDateTime())))
  }

  def removeConversation(userId: UID, conversationInfoId: UID, version: Int) {
    eventStore.addEvent(new EventRow(userId, conversationInfoId, version, new RemoveConversationForUserEvent))
  }

  def enterConversation(userId: UID, conversationInfoId: UID, version: Int) {
    eventStore.addEvent(new EventRow(userId, conversationInfoId, version, new EnterConversationForUserEvent))
  }

  def exitConversation(userId: UID, conversationInfoId: UID, version: Int) {
    eventStore.addEvent(new EventRow(userId, conversationInfoId, version, new ExitConversationForUserEvent))
  }


}
