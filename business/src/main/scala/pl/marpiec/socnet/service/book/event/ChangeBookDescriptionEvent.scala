package pl.marpiec.socnet.service.book.event

import pl.marpiec.socnet.model.book.BookDescription
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.BeanUtil

/**
 * @author Marcin Pieciukiewicz
 */

class ChangeBookDescriptionEvent(val bookDescription: BookDescription) extends Event {

  def entityClass = classOf[Book]

  def applyEvent(aggregate: Aggregate) {
    val book = aggregate.asInstanceOf[Book]
    BeanUtil.copyProperties(book.description, bookDescription)
  }
}
