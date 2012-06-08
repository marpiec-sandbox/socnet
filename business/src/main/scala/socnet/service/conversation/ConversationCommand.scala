package socnet.service.conversation

import pl.marpiec.util.UID
import collection.immutable.List

/**
 * @author Marcin Pieciukiewicz
 */

trait ConversationCommand {
  def createConversation(userId:UID, title: String, participantsUserIds: List[UID], newConversationId: UID)
  def createMessage(userId: UID, id: UID, version: Int, messageText: String, messageId: UID)
}
