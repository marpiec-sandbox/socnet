package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.ConversationInfo


/**
 * @author Marcin Pieciukiewicz
 */

trait ConversationInfoDatabase {
  def getConversationInfo(userId: UID, conversationId: UID): Option[ConversationInfo]

  def getConversationInfoList(userIdConversationId: List[(UID, UID)]): List[ConversationInfo]
}
