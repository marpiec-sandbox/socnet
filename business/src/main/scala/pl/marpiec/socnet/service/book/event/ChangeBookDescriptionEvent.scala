package pl.marpiec.socnet.service.book.event

import pl.marpiec.socnet.model.book.BookDescription
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * @author Marcin Pieciukiewicz
 */

class ChangeBookDescriptionEvent(bookDescription: BookDescription) extends Event {

  val title = bookDescription.title
  val polishTitle = bookDescription.polishTitle
  val authors = bookDescription.authors.toArray
  val description = bookDescription.description
  val isbn = bookDescription.isbn

  def entityClass = classOf[Book]

  def applyEvent(aggregate: Aggregate) {
    val book = aggregate.asInstanceOf[Book]

    book.description.title = title
    book.description.polishTitle = polishTitle
    book.description.authors = authors.toList
    book.description.description = description
    book.description.isbn = isbn
  }
}
