package socnet.readdatabase

import pl.marpiec.util.UID
import socnet.model.{Conversation, UserContacts}

/**
 * @author Marcin Pieciukiewicz
 */

trait ConversationDatabase {
  def addConversation(conversation: Conversation)

  def getConversationById(id: UID):Option[Conversation]
  def getConversationsByParticipantUserId(id: UID):List[Conversation]
}
