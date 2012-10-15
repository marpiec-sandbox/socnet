package pl.marpiec.socnet.service.booksuggestion.event

import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.BookSuggestion
import pl.marpiec.socnet.model.booksuggestion.BookSuggestionResponse

/**
 * @author Marcin Pieciukiewicz
 */

class DeclineBookSuggestionEvent(val comment: String, val responseTime: LocalDateTime) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val bookSuggestion = aggregate.asInstanceOf[BookSuggestion]
    val response = new BookSuggestionResponse
    response.commentOption = Option(comment)
    response.decision = BookSuggestionResponse.DECLINED
    response.time = responseTime

    bookSuggestion.responseOption = Option(response)
  }

  def entityClass = classOf[BookSuggestion]
}
