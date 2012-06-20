package socnet.readdatabase

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.AbstractDatabase
import socnet.model.ConversationInfo
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, DataStore}

/**
 * @author Marcin Pieciukiewicz
 */

class ConversationInfoDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[ConversationInfo](dataStore) with ConversationInfoDatabase {

  val USER_ID_CONVERSATION_ID_INDEX = "userConv"

  startListeningToDataStore(dataStore, classOf[ConversationInfo])

  addIndex(USER_ID_CONVERSATION_ID_INDEX, (aggregate: Aggregate) => {
    val conversationInfo = aggregate.asInstanceOf[ConversationInfo]
    (conversationInfo.userId, conversationInfo.conversationId)
  });

  def getConversationInfo(userId: UID, conversationId: UID): Option[ConversationInfo] = {
    getByIndex(USER_ID_CONVERSATION_ID_INDEX, (userId, conversationId))
  }
}
