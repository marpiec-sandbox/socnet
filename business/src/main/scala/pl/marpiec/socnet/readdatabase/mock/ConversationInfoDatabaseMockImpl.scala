package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.model.ConversationInfo
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.readdatabase.ConversationInfoDatabase

/**
 * @author Marcin Pieciukiewicz
 */


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

  def getConversationInfoList(userId: UID, conversationId: List[UID]) = {

    getAll.filter(info => {

      val userIdConversationIdOption = conversationId.find(conversationId => {
        info.userId == userId && info.conversationId == conversationId
      })

      userIdConversationIdOption.isDefined
    })

  }
}
