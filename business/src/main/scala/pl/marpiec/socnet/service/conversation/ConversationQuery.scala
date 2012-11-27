package pl.marpiec.socnet.service.conversation

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{Conversation}
import pl.marpiec.socnet.sql.entity.ConversationInfo

/**
 * @author Marcin Pieciukiewicz
 */

trait ConversationQuery {

  def loadConversationsOfUser(userId:UID):(List[Conversation], Map[UID, ConversationInfo])

}
