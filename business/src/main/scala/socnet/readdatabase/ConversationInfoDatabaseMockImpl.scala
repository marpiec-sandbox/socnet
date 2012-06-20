package socnet.readdatabase

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.AbstractDatabase
import socnet.model.ConversationInfo
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, DataStore}
import org.springframework.stereotype.Repository

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("conversationInfoDatabase")
class ConversationInfoDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[ConversationInfo](dataStore) with ConversationInfoDatabase {

  val USER_ID_CONVERSATION_ID_INDEX = "userIdConversationId"

  startListeningToDataStore(dataStore, classOf[ConversationInfo])

  addIndex(USER_ID_CONVERSATION_ID_INDEX, (aggregate: Aggregate) => {
    val conversationInfo = aggregate.asInstanceOf[ConversationInfo]
    (conversationInfo.userId, conversationInfo.conversationId)
  });

  def getConversationInfo(userId: UID, conversationId: UID): Option[ConversationInfo] = {
    getByIndex(USER_ID_CONVERSATION_ID_INDEX, (userId, conversationId))
  }

  def getConversationInfoList(userIdConversationIdList: List[(UID, UID)]) = {

    getAll.filter(info => {

      val userIdConversationIdOption = userIdConversationIdList.find((userIdConversationId:(UID, UID)) => {
        val (userId, conversationId) = userIdConversationId
        info.userId == userId && info.conversationId == conversationId
      })

      userIdConversationIdOption.isDefined
    })

  }
}
