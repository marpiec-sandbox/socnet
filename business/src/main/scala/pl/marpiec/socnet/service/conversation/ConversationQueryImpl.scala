package pl.marpiec.socnet.service.conversation

import org.springframework.stereotype.Service
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{Conversation}
import pl.marpiec.socnet.readdatabase.{ConversationDatabase}
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.sql.dao.ConversationInfoDao
import pl.marpiec.socnet.sql.entity.ConversationInfo

/**
 * @author Marcin Pieciukiewicz
 */

@Service("conversationQuery")
class ConversationQueryImpl @Autowired()(val conversationDatabase: ConversationDatabase, val conversationInfoDao: ConversationInfoDao) extends ConversationQuery {

  def loadConversationsOfUser(userId: UID): (List[Conversation], Map[UID, ConversationInfo]) = {
    val userConversations: List[Conversation] = conversationDatabase.getConversationsByParticipantOrInvitedUserId(userId)
    val conversationInfoMap: Map[UID, ConversationInfo] = loadConversationInfo(userId, userConversations)
    (userConversations, conversationInfoMap)
  }


  private def loadConversationInfo(userId: UID, conversations: List[Conversation]): Map[UID, ConversationInfo] = {

    val conversationIdList: List[UID] = prepareConversationIdsList(conversations, userId)
    val conversationInfoList: List[ConversationInfo] = conversationInfoDao.readMany(userId, conversationIdList)
    createConversationInfoMapFromList(conversationInfoList)

  }

  private def prepareConversationIdsList(conversations: List[Conversation], userId: UID): List[UID] = {
    var conversationIdList: List[UID] = List()

    conversations.foreach(conversation => {
      conversationIdList ::= conversation.id
    })
    conversationIdList
  }

  private def createConversationInfoMapFromList(conversationInfoList: List[ConversationInfo]): Map[UID, ConversationInfo] = {
    var conversationsMap: Map[UID, ConversationInfo] = Map()

    conversationInfoList.foreach(conversationInfo => {
      conversationsMap += conversationInfo.conversationId -> conversationInfo
    })

    conversationsMap
  }
}
