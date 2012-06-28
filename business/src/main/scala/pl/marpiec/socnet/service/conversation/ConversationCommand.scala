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

  def addParticipant(userId: UID, id: UID, version: Int, message: String, addedParticipantUserId: UID)

  def userHasReadConversation(userId: UID, conversationInfoId: UID, version: Int)

  def removeConversation(userId: UID, conversationInfoId: UID, version: Int)

  def enterConversation(userId: UID, conversationInfoId: UID, version: Int)

  def exitConversation(userId: UID, conversationInfoId: UID, version: Int)


}
