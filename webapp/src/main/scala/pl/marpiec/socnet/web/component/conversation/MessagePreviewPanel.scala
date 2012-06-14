package pl.marpiec.socnet.web.component.conversation

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.util.BBCodeConverter

/**
 * @author Marcin Pieciukiewicz
 */

class MessagePreviewPanel(id:String, val messageText:String) extends Panel(id) {

  val convertedMessage = BBCodeConverter.convert(messageText)

  add(new Label("messageText", convertedMessage).setEscapeModelStrings(false));

}
