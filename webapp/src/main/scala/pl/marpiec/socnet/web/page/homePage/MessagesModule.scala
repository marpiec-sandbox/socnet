package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.component.conversation.RecentConversationsSummaryPanel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.conversation.{StartConversationPage, UserConversationsPage}

/**
 * @author Marcin Pieciukiewicz
 */

class MessagesModule(id:String) extends Panel(id) {

   add(new RecentConversationsSummaryPanel("recentMessages"))

  add(new BookmarkablePageLink("newConversationLink", classOf[StartConversationPage]))
  add(new BookmarkablePageLink("conversationsLink", classOf[UserConversationsPage]))
}
