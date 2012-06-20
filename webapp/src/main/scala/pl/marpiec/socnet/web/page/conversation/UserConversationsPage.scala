package pl.marpiec.socnet.web.page.conversation

import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.socnet.web.authorization.SecureWebPage
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.repeater.RepeatingView
import pl.marpiec.socnet.readdatabase.UserDatabase
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.component.conversation.ConversationSummaryPanel
import socnet.readdatabase.{ConversationInfoDatabase, ConversationDatabase}
import socnet.model.{ConversationInfo, Conversation}
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class UserConversationsPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean
  private var conversationDatabase: ConversationDatabase = _

  @SpringBean
  private var userDatabase: UserDatabase = _

  @SpringBean
  private var conversationInfoDatabase: ConversationInfoDatabase = _
  
  
  val userConversations: List[Conversation] = conversationDatabase.getConversationsByParticipantUserId(session.userId())
  val conversationInfoMap: Map[UID, ConversationInfo] = loadConversationInfo(userConversations)
  

  add(new RepeatingView("conversation") {

    userConversations.foreach(conversation => {

      val users = userDatabase.getUsersByIds(conversation.participantsUserIds)

      add(new AbstractItem(newChildId()) {
        add(new ConversationSummaryPanel("conversationSummary", conversation, users, conversationInfoMap.get(conversation.id)))
      })

    })
  })



  private def loadConversationInfo(conversations: List[Conversation]): Map[UID, ConversationInfo] = {

    val userId = session.userId
    
    var userIdConversationIdList: scala.List[(UID, UID)] = prepareKeyList(conversations, userId)
    val conversationInfoList:List[ConversationInfo] = conversationInfoDatabase.getConversationInfoList(userIdConversationIdList)
    createConversationInfoMapFromList(conversationInfoList)
    
  }

  private def prepareKeyList(conversations: List[Conversation], userId: UID): List[(UID, UID)] = {
    var userIdConversationIdList: List[(UID, UID)] = List()

    conversations.foreach(conversation => {
      userIdConversationIdList ::=(userId, conversation.id)
    })
    userIdConversationIdList
  }

  private def createConversationInfoMapFromList(conversationInfoList:List[ConversationInfo]): Map[UID, ConversationInfo] = {
    var conversationsMap: Map[UID, ConversationInfo] = Map()

    conversationInfoList.foreach(conversationInfo => {
      conversationsMap += conversationInfo.conversationId -> conversationInfo
    })

    conversationsMap
  }

  
}
