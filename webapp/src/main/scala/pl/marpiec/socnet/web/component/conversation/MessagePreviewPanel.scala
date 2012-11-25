package pl.marpiec.socnet.web.component.conversation

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.model.conversation.Message
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import pl.marpiec.util.{StringFormattingUtil, BBCodeConverter}
import org.apache.wicket.{Component, AttributeModifier}
import org.apache.wicket.model.Model

/**
 * @author Marcin Pieciukiewicz
 */

class MessagePreviewPanel(id: String, message: Message, user: User, notYetRead: Boolean) extends Panel(id) {

  val convertedMessage = BBCodeConverter.convert(message.messageText)

  if(notYetRead) {
    add(new AttributeModifier("class", "notYetRead"))
  }

  add(UserProfilePreviewPage.getLink("profileLink", user).add(new Label("userName", user.fullName)))
  add(new Label("sentTime", StringFormattingUtil.printDateTime(message.sentTime)))
  add(new Label("messageText", convertedMessage).setEscapeModelStrings(false))


}
