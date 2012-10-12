package pl.marpiec.socnet.model.book

import scala.collection.JavaConversions._
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

class BookDescription {

  var title:String = _
  var polishTitle:String = _
  var authors:List[String] = Nil
  var description:String = _
  var isbn:String = _

  def getFormattedAuthorsString:String = StringUtils.join(authors, ", ")
  
}
