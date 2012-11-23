package pl.marpiec.socnet.web.component.conversation

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.User
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.conversation.ConversationPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.util.{UID, StringFormattingUtil}
import pl.marpiec.socnet.model.{ConversationInfo, Conversation}

/**
 * @author Marcin Pieciukiewicz
 */

class ConversationSummaryPanel(id: String, val conversation: Conversation, users: List[User],
                               conversationInfoOption: Option[ConversationInfo]) extends Panel(id) {


  add(createLinkToConversation.add(new Label("conversationTitle", conversation.title)))
  add(new Label("creationTime", StringFormattingUtil.printDateTime(conversation.messages.last.sentTime)))
  add(new Label("lastMessageUsername", getUserById(conversation.messages.head.authorUserId).fullName))
  add(new Label("lastMessageTime", StringFormattingUtil.printDateTime(conversation.messages.head.sentTime)))
  add(new Label("unreadMessagesCount", conversation.calculateUnreadMessagesCount(conversationInfoOption).toString))


  add(new RepeatingView("participant") {
    users.foreach(user => {
      add(new AbstractItem(newChildId()) {
        add(new Label("userName", user.fullName))
      })
    })
  })

  def getUserById(userId: UID): User = {
    users.find(user => user.id == userId).get
  }

  def createLinkToConversation: BookmarkablePageLink[_] = {
    ConversationPage.getLink("conversationLink", conversation.id)
  }
}
