package pl.marpiec.socnet.web.component.wicket.autocomplete

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer
import org.apache.wicket.request.Response
import org.apache.wicket.util.string.Strings
import pl.marpiec.util.StringFormattingUtil

/**
 * Implementation based on standard Wicket StringAutoCompleteRenderer
 * @author Marcin Pieciukiewicz
 */

object BoldingAutoCompleteRenderer{
  val INSTANCE = new BoldingAutoCompleteRenderer
}

class BoldingAutoCompleteRenderer extends AbstractAutoCompleteTextRenderer[String] {
  def getTextValue(textValue: String) = textValue

  override def renderChoice(obj: String, response: Response, criteria: String) {
    val trimmedCriteria = criteria.trim()
    val textValue = Strings.escapeMarkup(obj).toString
    
    val indexStart = StringFormattingUtil.toLowerCase(textValue).indexOf(StringFormattingUtil.toLowerCase(trimmedCriteria))
    if (indexStart >= 0) {

      val indexEnd = indexStart + trimmedCriteria.length()

      val markedTextValue = textValue.substring(0, indexStart) + "<span class='matched'>" +
        textValue.substring(indexStart, indexEnd) + "</span>" + textValue.substring(indexEnd, textValue.length())
      response.write(markedTextValue)

    } else {
      response.write(textValue)
    }
    

  }

}
