package pl.marpiec.socnet.web.component.conversation

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import socnet.model.conversation.Message
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.web.page.UserProfilePreviewPage
import pl.marpiec.util.{DateUtil, BBCodeConverter}

/**
 * @author Marcin Pieciukiewicz
 */

class MessagePreviewPanel(id:String, val message:Message, val user:User) extends Panel(id) {

  val convertedMessage = BBCodeConverter.convert(message.messageText)

  add(UserProfilePreviewPage.getLink(user).add(new Label("userName", user.fullName)))
  add(new Label("sentTime", DateUtil.printDateTime(message.sentTime)))
  add(new Label("messageText", convertedMessage).setEscapeModelStrings(false))
  

}
