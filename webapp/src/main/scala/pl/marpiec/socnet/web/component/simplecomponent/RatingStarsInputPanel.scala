package pl.marpiec.socnet.web.component.simplecomponent

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.ComponentTag

/**
 * @author Marcin Pieciukiewicz
 */

class RatingStarsInputPanel(id:String, inputCssClass:String) extends Panel(id) {
  
  add(new WebMarkupContainer("ratingStarsInputHolder") {
    override def onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("alt", inputCssClass)
    }
  })

}
