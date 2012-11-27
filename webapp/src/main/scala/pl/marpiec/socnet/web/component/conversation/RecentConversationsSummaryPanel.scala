package pl.marpiec.socnet.web.component.conversation

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.UserDatabase
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{Conversation}
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.service.conversation.ConversationQuery
import pl.marpiec.socnet.sql.entity.ConversationInfo

/**
 * @author Marcin Pieciukiewicz
 */

class RecentConversationsSummaryPanel(id: String) extends Panel(id) {

  @SpringBean private var userDatabase: UserDatabase = _
  @SpringBean private var conversationQuery: ConversationQuery = _

  val session = getSession.asInstanceOf[SocnetSession]

  if (session.isAuthenticated) {

    val (userConversations: List[Conversation], conversationInfoMap: Map[UID, ConversationInfo]) = conversationQuery.loadConversationsOfUser(session.userId)

    add(new RepeatingView("conversation") {

      userConversations.foreach(conversation => {

        val users = userDatabase.getUsersByIds((conversation.participantsUserIds ++ conversation.invitedUserIds ++ conversation.previousUserIds).toList)

        add(new AbstractItem(newChildId()) {
          add(new SimpleConversationSummaryPanel("conversationSummary", conversation, users, conversationInfoMap.get(conversation.id)))
        })

      })
    })

  }


}
