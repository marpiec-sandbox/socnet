package socnet.readdatabase

import pl.marpiec.util.UID
import socnet.model.{ConversationInfo, UserContacts}


/**
 * @author Marcin Pieciukiewicz
 */

trait ConversationInfoDatabase {
  def getConversationInfo(userId: UID, conversationId: UID):Option[ConversationInfo]
}
