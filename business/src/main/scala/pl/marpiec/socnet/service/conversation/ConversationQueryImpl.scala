package pl.marpiec.socnet.service.conversation

import org.springframework.stereotype.Service
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{ConversationInfo, Conversation}
import pl.marpiec.socnet.readdatabase.{ConversationInfoDatabase, UserDatabase, ConversationDatabase}
import org.springframework.beans.factory.annotation.{Autowired, Autowire}

/**
 * @author Marcin Pieciukiewicz
 */

@Service("conversationQuery")
class ConversationQueryImpl @Autowired() (val conversationDatabase: ConversationDatabase,
                                          conversationInfoDatabase: ConversationInfoDatabase) extends ConversationQuery {

  def loadConversationsOfUser(userId:UID):(List[Conversation], Map[UID, ConversationInfo]) = {
    val userConversations: List[Conversation] = conversationDatabase.getConversationsByParticipantUserId(userId)
    val conversationInfoMap: Map[UID, ConversationInfo] = loadConversationInfo(userId, userConversations)
    (userConversations, conversationInfoMap)
  }


  private def loadConversationInfo(userId:UID, conversations: List[Conversation]): Map[UID, ConversationInfo] = {

    val userIdConversationIdList: scala.List[(UID, UID)] = prepareKeyList(conversations, userId)
    val conversationInfoList: List[ConversationInfo] = conversationInfoDatabase.getConversationInfoList(userIdConversationIdList)
    createConversationInfoMapFromList(conversationInfoList)

  }

  private def prepareKeyList(conversations: List[Conversation], userId: UID): List[(UID, UID)] = {
    var userIdConversationIdList: List[(UID, UID)] = List()

    conversations.foreach(conversation => {
      userIdConversationIdList ::=(userId, conversation.id)
    })
    userIdConversationIdList
  }

  private def createConversationInfoMapFromList(conversationInfoList: List[ConversationInfo]): Map[UID, ConversationInfo] = {
    var conversationsMap: Map[UID, ConversationInfo] = Map()

    conversationInfoList.foreach(conversationInfo => {
      conversationsMap += conversationInfo.conversationId -> conversationInfo
    })

    conversationsMap
  }
}
