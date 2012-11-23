package pl.marpiec.socnet.service.conversation

import org.springframework.stereotype.Service
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{ConversationInfo, Conversation}
import pl.marpiec.socnet.readdatabase.{ConversationInfoDatabase, ConversationDatabase}
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author Marcin Pieciukiewicz
 */

@Service("conversationQuery")
class ConversationQueryImpl @Autowired()(val conversationDatabase: ConversationDatabase,
                                         conversationInfoDatabase: ConversationInfoDatabase) extends ConversationQuery {

  def loadConversationsOfUser(userId: UID): (List[Conversation], Map[UID, ConversationInfo]) = {
    val userConversations: List[Conversation] = conversationDatabase.getConversationsByParticipantOrInvitedUserId(userId)
    val conversationInfoMap: Map[UID, ConversationInfo] = loadConversationInfo(userId, userConversations)
    (userConversations, conversationInfoMap)
  }


  private def loadConversationInfo(userId: UID, conversations: List[Conversation]): Map[UID, ConversationInfo] = {

    val conversationIdList: List[UID] = prepareConversationIdsList(conversations, userId)
    val conversationInfoList: List[ConversationInfo] = conversationInfoDatabase.getConversationInfoList(userId, conversationIdList)
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
