package pl.marpiec.socnet.service.book.event

import pl.marpiec.socnet.model.book.BookDescription
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.BeanUtil
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class CreateBookEvent(val bookDescription: BookDescription, val creationTime:LocalDateTime) extends Event {

  def entityClass = classOf[Book]

  def applyEvent(aggregate: Aggregate) {
    val book = aggregate.asInstanceOf[Book]
    book.creationTime = creationTime
    BeanUtil.copyProperties(book.description, bookDescription)
  }
}