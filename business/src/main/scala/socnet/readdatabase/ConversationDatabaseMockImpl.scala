package socnet.readdatabase

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.DataStore
import pl.marpiec.socnet.readdatabase.AbstractDatabase
import socnet.model.{Conversation, UserContacts}
import org.springframework.stereotype.Repository
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("conversationDatabase")
class ConversationDatabaseMockImpl @Autowired() (dataStore: DataStore) extends AbstractDatabase[Conversation](dataStore) with ConversationDatabase {

  startListeningToDataStore(dataStore, classOf[Conversation])

  def addConversation(conversation: Conversation) = add(conversation)

  def getConversationById(id: UID) = getById(id)

  def getConversationsByParticipantUserId(id: UID):List[Conversation] = {
    var conversationsForUser:List[Conversation] = Nil
    for (conversation <- getAll) {
      for (userId <- conversation.participantsUserIds) {
        if (userId == id) {
          conversationsForUser ::= conversation
        }
      }
    }
    conversationsForUser
  }
}
