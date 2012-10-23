package pl.marpiec.socnet.web.component.simplecomponent

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.util.StringFormattingUtil

/**
 * @author Marcin Pieciukiewicz
 */

class RatingStarsPanel(id:String, rating:Double) extends Panel(id) {
  add(new Label("rating", StringFormattingUtil.printSimpleDoubleForJavascript(rating)))
}
