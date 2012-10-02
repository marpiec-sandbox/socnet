package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.ConversationInfoDatabase
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, DataStoreListener, DataStore}
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import pl.marpiec.socnet.model.ConversationInfo
import org.springframework.stereotype.Repository
import com.mongodb.QueryBuilder

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("conversationInfoDatabase")
class ConversationInfoDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener with ConversationInfoDatabase {

  val connector = new DatabaseConnectorImpl("conversationsInfos")

  startListeningToDataStore(dataStore, classOf[ConversationInfo])

  def onEntityChanged(entity: Aggregate) {
    connector.insertAggregate(entity.asInstanceOf[ConversationInfo])
  }

  def getConversationInfo(userId: UID, conversationId: UID): Option[ConversationInfo] = {
    connector.findAggregateByQuery(QueryBuilder.start("userId").is(userId.uid).
                                   and("conversationId").is(conversationId.uid).get(),
      classOf[ConversationInfo])
  }

  def getConversationInfoList(userId: UID, conversationId: List[UID]): List[ConversationInfo] = {

    val conversationsSimpleIds = new Array[Long](conversationId.size)
    
    for (i <- 0 until conversationId.size) {
      conversationsSimpleIds(i) = conversationId(i).uid
    }

    connector.findMultipleAggregatesByQuery(QueryBuilder.start("userId").is(userId.uid).
      and("conversationId").in(conversationsSimpleIds).get(), classOf[ConversationInfo])
  }
}
