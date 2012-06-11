package pl.marpiec.socnet.web.component.conversation

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.util.UID
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.conversation.StartConversationPage
import org.apache.wicket.request.mapper.parameter.PageParameters

/**
 * @author Marcin Pieciukiewicz
 */


class StartConversationPanel(id:String, val userId:UID) extends Panel(id) {

  add(new BookmarkablePageLink("newConversationLink", classOf[StartConversationPage],
    new PageParameters().add(StartConversationPage.USER_ID_PARAM, userId.uid)))
  
}

