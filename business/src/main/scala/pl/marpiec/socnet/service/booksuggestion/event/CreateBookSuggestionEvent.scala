package pl.marpiec.socnet.service.booksuggestion.event

import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.BookSuggestion
import pl.marpiec.cqrs.{Event, Aggregate}

/**
 * @author Marcin Pieciukiewicz
 */

class CreateBookSuggestionEvent(bookDescription: BookDescription, val userComment:String,
                                val userId: UID, val creationTime: LocalDateTime) extends Event {

  val title = bookDescription.title
  val polishTitle = bookDescription.polishTitle
  val authors = bookDescription.authors
  val description = bookDescription.description
  val isbn = bookDescription.isbn

  def entityClass = classOf[BookSuggestion]

  def applyEvent(aggregate: Aggregate) {
    val bookSuggestion = aggregate.asInstanceOf[BookSuggestion]

    bookSuggestion.creationTime = creationTime
    bookSuggestion.userId = userId
    bookSuggestion.userComment = userComment

    bookSuggestion.title = title
    bookSuggestion.polishTitle = polishTitle
    bookSuggestion.authors = authors
    bookSuggestion.description = description
    bookSuggestion.isbn = isbn

  }
}