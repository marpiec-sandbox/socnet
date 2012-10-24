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
    index ++= SearchUtils.queryToWordsList(description.getFormattedAuthorsString)
    index ++= SearchUtils.queryToWordsList(description.title)
    index ++= SearchUtils.queryToWordsList(description.polishTitle)
    index ++= SearchUtils.queryToWordsList(description.description)
    index ++= SearchUtils.queryToWordsList(description.isbn)
    SearchUtils.prepareIndex(index)
  }

}
