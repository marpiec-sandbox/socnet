package pl.marpiec.socnet.service.book.event

import pl.marpiec.socnet.service.book.input.BookOwnershipInput
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.{UID, BeanUtil}
import pl.marpiec.socnet.model.book.BookOwnership

/**
 * @author Marcin Pieciukiewicz
 */

class AddOrUpdateBookOwnership(val userId:UID, val bookOwnershipInput: BookOwnershipInput) extends Event {
  def entityClass = classOf[Book]

  def applyEvent(aggregate: Aggregate) {
    val book = aggregate.asInstanceOf[Book]

    val ownership = book.ownership.get(userId).getOrElse({
      val newOwnership = new BookOwnership
      book.ownership += userId -> newOwnership
      newOwnership
    })
    
    BeanUtil.copyProperties(ownership, bookOwnershipInput)
  }
}
