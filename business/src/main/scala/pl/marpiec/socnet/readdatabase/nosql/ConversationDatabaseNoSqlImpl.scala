package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.ConversationDatabase
import pl.marpiec.cqrs.{Aggregate, DataStoreListener, DataStore}
import pl.marpiec.util.UID
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import pl.marpiec.socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

class ConversationDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener with ConversationDatabase {

  val connector = new DatabaseConnectorImpl("conversations")

  startListeningToDataStore(dataStore, classOf[Conversation])

  def addConversation(conversation: Conversation) {
    connector.insertAggregate(conversation)
  }

  def getConversationById(id: UID) = connector.getAggregateById(id, classOf[Conversation])

  def getConversationsByParticipantUserId(id: UID): List[Conversation] = {
    //
  }

  def onEntityChanged(entity: Aggregate) {
    connector.insertAggregate(entity.asInstanceOf[Conversation])
  }
}
