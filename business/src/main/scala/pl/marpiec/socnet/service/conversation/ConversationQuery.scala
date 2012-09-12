package pl.marpiec.socnet.service.conversation

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{ConversationInfo, Conversation}

/**
 * @author Marcin Pieciukiewicz
 */

trait ConversationQuery {

  def loadConversationsOfUser(userId:UID):(List[Conversation], Map[UID, ConversationInfo])

}
