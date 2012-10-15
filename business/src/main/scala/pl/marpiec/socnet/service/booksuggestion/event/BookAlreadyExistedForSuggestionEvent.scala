package pl.marpiec.socnet.service.booksuggestion.event

import pl.marpiec.util.UID
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.BookSuggestion
import pl.marpiec.socnet.model.booksuggestion.BookSuggestionResponse

/**
 * @author Marcin Pieciukiewicz
 */

class BookAlreadyExistedForSuggestionEvent(val existingBookId: UID, val responseTime: LocalDateTime) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val bookSuggestion = aggregate.asInstanceOf[BookSuggestion]
    val response = new BookSuggestionResponse
    response.bookIdOption = Option(existingBookId)
    response.decision = BookSuggestionResponse.ALREADY_EXISTED
    response.time = responseTime

    bookSuggestion.responseOption = Option(response)
  }

  def entityClass = classOf[BookSuggestion]
}
