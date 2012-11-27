package pl.marpiec.socnet.service.conversation

import pl.marpiec.util.UID
import collection.immutable.List

/**
 * @author Marcin Pieciukiewicz
 */

trait ConversationCommand {

  def createConversation(userId: UID, title: String, participantsUserIds: List[UID], newConversationId: UID,
                         firstMessageText: String, firstMessageId: UID)

  def createMessage(userId: UID, id: UID, version: Int, messageText: String, messageId: UID)

  def addParticipants(userId: UID, id: UID, version: Int, addedParticipantsUserIds: List[UID])

  def removeConversationForUser(userId: UID, conversationId: UID, version: Int, removingUserId: UID)

  def enterConversation(userId: UID, conversationId: UID, version: Int, enteringUserId: UID)

  def exitConversation(userId: UID, conversationId: UID, version: Int, exitingUserId: UID)


}
