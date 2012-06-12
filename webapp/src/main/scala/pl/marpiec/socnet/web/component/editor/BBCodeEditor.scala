package pl.marpiec.socnet.web.component.editor

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.form.TextArea
import org.apache.wicket.model.PropertyModel

/**
 * @author Marcin Pieciukiewicz
 */

class BBCodeEditor(id: String, propertyModel: PropertyModel[String]) extends Panel(id) {

  add(new TextArea[String]("textInput", propertyModel))

}
