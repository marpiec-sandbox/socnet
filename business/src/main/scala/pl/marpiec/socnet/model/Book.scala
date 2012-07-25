package pl.marpiec.socnet.model

import book.{BookOwnership, BookReviews, BookDescription}
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{UID, BeanUtil}

/**
 * @author Marcin Pieciukiewicz
 */

class Book extends Aggregate(null, 0) {

  var description = new BookDescription
  var reviews = new BookReviews
  var ownership: Map[UID, BookOwnership] = Map()


  def copy: Aggregate = {
    BeanUtil.copyProperties(new Book, this)
  }
}
