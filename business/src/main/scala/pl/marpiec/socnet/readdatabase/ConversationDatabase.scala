package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

trait ConversationDatabase {
  def addConversation(conversation: Conversation)

  def getConversationById(id: UID): Option[Conversation]

  def getConversationsByParticipantOrInvitedUserId(id: UID): List[Conversation]
}
