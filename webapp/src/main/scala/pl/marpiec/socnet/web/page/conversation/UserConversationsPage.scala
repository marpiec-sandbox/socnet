package pl.marpiec.socnet.web.page.conversation

import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.socnet.web.authorization.SecureWebPage
import org.apache.wicket.spring.injection.annot.SpringBean
import socnet.readdatabase.ConversationDatabase
import socnet.model.Conversation
import org.apache.wicket.markup.repeater.RepeatingView
import pl.marpiec.socnet.readdatabase.UserDatabase
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.component.conversation.ConversationSummaryPanel

/**
 * @author Marcin Pieciukiewicz
 */

class UserConversationsPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean
  private var conversationDatabase: ConversationDatabase = _

  @SpringBean
  private var userDatabase: UserDatabase = _

  val userConversations: List[Conversation] = conversationDatabase.getConversationsByParticipantUserId(session.userId())


  add(new RepeatingView("conversation") {

    userConversations.foreach(conversation => {

      val users = userDatabase.getUsersByIds(conversation.participantsUserIds)


      add(new AbstractItem(newChildId()) {

        add(new ConversationSummaryPanel("conversationSummary", conversation, users))

      })


    })
  })

}
