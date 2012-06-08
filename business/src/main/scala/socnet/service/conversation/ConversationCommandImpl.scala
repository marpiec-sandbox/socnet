package socnet.service.conversation

import event.{CreateMessageEvent, CreateConversationEvent}
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.util.UID
import collection.immutable.List
import pl.marpiec.cqrs.{EventRow, EventStore}
import org.springframework.stereotype.Service

/**
 * @author Marcin Pieciukiewicz
 */

@Service("conversationCommand")
class ConversationCommandImpl @Autowired()(val eventStore: EventStore) extends ConversationCommand {

  def createConversation(userId: UID, title: String, participantsUserIds: List[UID], newConversationId: UID) {
    val createConversation = new CreateConversationEvent(userId, title, participantsUserIds)
    eventStore.addEventForNewAggregate(newConversationId, new EventRow(userId, newConversationId, 0, createConversation))
  }

  def createMessage(userId: UID, id: UID, version: Int, messageText: String, messageId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new CreateMessageEvent(userId, messageText, messageId)))

  }

}
