package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.DataStore
import pl.marpiec.socnet.model.Conversation
import pl.marpiec.util.UID
import pl.marpiec.socnet.readdatabase.ConversationDatabase

/**
 * @author Marcin Pieciukiewicz
 */


class ConversationDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[Conversation](dataStore) with ConversationDatabase {

  startListeningToDataStore(dataStore, classOf[Conversation])

  def addConversation(conversation: Conversation) {
    add(conversation)
  }

  def getConversationById(id: UID): Option[Conversation] = getById(id)

  def getConversationsByParticipantUserId(id: UID): List[Conversation] = {
    getAll.filter(conversation => {
      conversation.participantsUserIds.contains(id)
    })
  }
}
