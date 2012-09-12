package pl.marpiec.socnet.web.page.conversation

import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.socnet.web.authorization.SecureWebPage
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.repeater.RepeatingView
import pl.marpiec.socnet.readdatabase.UserDatabase
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.component.conversation.ConversationSummaryPanel
import pl.marpiec.socnet.readdatabase.{ConversationInfoDatabase, ConversationDatabase}
import pl.marpiec.socnet.model.{ConversationInfo, Conversation}
import pl.marpiec.util.UID
import pl.marpiec.socnet.service.conversation.ConversationQuery

/**
 * @author Marcin Pieciukiewicz
 */

class UserConversationsPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var userDatabase: UserDatabase = _
  @SpringBean private var conversationQuery: ConversationQuery = _

  val (userConversations: List[Conversation], conversationInfoMap: Map[UID, ConversationInfo]) = conversationQuery.loadConversationsOfUser(session.userId)

  add(new RepeatingView("conversation") {

    userConversations.foreach(conversation => {

      val users = userDatabase.getUsersByIds(conversation.participantsUserIds)

      add(new AbstractItem(newChildId()) {
        add(new ConversationSummaryPanel("conversationSummary", conversation, users, conversationInfoMap.get(conversation.id)))
      })

    })
  })




}
