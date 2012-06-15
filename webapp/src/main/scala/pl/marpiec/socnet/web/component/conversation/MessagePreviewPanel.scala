package pl.marpiec.socnet.web.component.conversation

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.util.BBCodeConverter
import socnet.model.conversation.Message

/**
 * @author Marcin Pieciukiewicz
 */

class MessagePreviewPanel(id:String, val message:Message) extends Panel(id) {

  val convertedMessage = BBCodeConverter.convert(message.messageText)

  add(new Label("messageText", convertedMessage).setEscapeModelStrings(false));

}
