package pl.marpiec.socnet.web.component.conversation

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.User
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.conversation.ConversationPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.util.{UID, StringFormattingUtil}
import pl.marpiec.socnet.model.{ConversationInfo, Conversation}

/**
 * @author Marcin Pieciukiewicz
 */

class SimpleConversationSummaryPanel(id: String, val conversation: Conversation, users: List[User],
                                     conversationInfoOption: Option[ConversationInfo]) extends Panel(id) {


  val unreadMessageCount = conversation.calculateUnreadMessagesCount(conversationInfoOption)

  add(new BookmarkablePageLink("conversationLink", classOf[ConversationPage], createParametersForLink) {
    add(new Label("conversationTitle", conversation.title))
    add(new Label("lastMessageUsername", getUserById(conversation.messages.head.authorUserId).fullName))
    add(new Label("lastMessageTime", StringFormattingUtil.printDateTime(conversation.messages.head.sentTime)))
    add(new Label("unreadMessagesCount", unreadMessageCount.toString).setVisible(unreadMessageCount > 0))
  })


  def getUserById(userId: UID): User = {
    users.find(user => user.id == userId).get
  }

  def createParametersForLink: PageParameters = {
    new PageParameters().add(ConversationPage.CONVERSATION_ID_PARAM, conversation.id)
  }
}
