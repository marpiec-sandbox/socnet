package pl.marpiec.socnet.model

import scala.collection.JavaConversions._
import booksuggestion.BookSuggestionResponse
import pl.marpiec.cqrs.Aggregate
import org.joda.time.LocalDateTime
import pl.marpiec.util.{UID, BeanUtil}
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */


class BookSuggestion extends Aggregate(null, 0) {

  var userId: UID = _
  var creationTime: LocalDateTime = _

  var title: String = _
  var polishTitle: String = _

  var isbn: String = _
  var description: String = _
  var authors: List[String] = Nil

  var userComment: String = _

  var responseOption: Option[BookSuggestionResponse] = None

  var removedFromUsersList = false

  def copy: Aggregate = {
    BeanUtil.copyProperties(new BookSuggestion, this)
  }

  def getFormattedAuthorsString: String = StringUtils.join(authors, ", ")
}
