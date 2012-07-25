package pl.marpiec.socnet.model

import book.{BookOwnership, BookReviews, BookDescription}
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{UID, BeanUtil}
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class Book extends Aggregate(null, 0) {

  var creationTime: LocalDateTime = _
  var description = new BookDescription
  var reviews = new BookReviews
  var ownership: Map[UID, BookOwnership] = Map()


  def copy: Aggregate = {
    BeanUtil.copyProperties(new Book, this)
  }
}
