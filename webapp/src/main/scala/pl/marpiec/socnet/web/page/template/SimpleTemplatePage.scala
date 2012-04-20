package pl.marpiec.socnet.web.page.template

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SimpleTemplatePage extends WebPage {

  val titleLabelModel = new Model[String]("Socnet")

  add(new Label("pageTitle", titleLabelModel))

  protected def setSubTitle(title:String) {
    titleLabelModel.setObject("Socnet "+title)
  }
}
