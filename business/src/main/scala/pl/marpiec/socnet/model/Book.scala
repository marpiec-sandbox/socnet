package pl.marpiec.socnet.model

import book.BookDescription
import pl.marpiec.cqrs.Aggregate
import org.joda.time.LocalDateTime
import pl.marpiec.util.{SearchUtils, BeanUtil}

/**
 * @author Marcin Pieciukiewicz
 */

class Book extends Aggregate(null, 0) {

  var creationTime: LocalDateTime = _
  var description = new BookDescription

  def copy: Aggregate = {
    BeanUtil.copyProperties(new Book, this)
  }

  def createIndex():List[String] = {
    var index = List[String]()
    index ++= description.getFormattedAuthorsString.split("[\\s\\,]+")
    index ++= description.title.split("[\\s\\,]+")
    index ++= description.polishTitle.split("[\\s\\,]+")
    index ++= description.description.split("[\\s\\,]+")
    index ::= description.isbn
    SearchUtils.prepareIndex(index)
  }

}
