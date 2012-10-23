package pl.marpiec.socnet.web.component.conversation

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.model.conversation.Message
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import pl.marpiec.util.{StringFormattingUtil, BBCodeConverter}

/**
 * @author Marcin Pieciukiewicz
 */

class MessagePreviewPanel(id: String, val message: Message, val user: User) extends Panel(id) {

  val convertedMessage = BBCodeConverter.convert(message.messageText)

  add(UserProfilePreviewPage.getLink(user).add(new Label("userName", user.fullName)))
  add(new Label("sentTime", StringFormattingUtil.printDateTime(message.sentTime)))
  add(new Label("messageText", convertedMessage).setEscapeModelStrings(false))


}
