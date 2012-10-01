package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.ConversationInfoDatabase
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, DataStoreListener, DataStore}
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import pl.marpiec.socnet.model.{Conversation, ConversationInfo}

/**
 * @author Marcin Pieciukiewicz
 */

class ConversationInfoDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener with ConversationInfoDatabase {

  val connector = new DatabaseConnectorImpl("conversationsInfos")

  startListeningToDataStore(dataStore, classOf[ConversationInfo])

  def onEntityChanged(entity: Aggregate) {
    connector.insertAggregate(entity.asInstanceOf[Conversation])
  }

  def getConversationInfo(userId: UID, conversationId: UID): Option[ConversationInfo] = {
    //
  }

  def getConversationInfoList(userIdConversationId: List[(UID, UID)]): List[ConversationInfo] = {
    //
  }
}
