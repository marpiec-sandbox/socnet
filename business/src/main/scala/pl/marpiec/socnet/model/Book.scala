package pl.marpiec.socnet.model

import book.{BookReviews, BookDescription}
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.BeanUtil

/**
 * @author Marcin Pieciukiewicz
 */

class Book extends Aggregate(null, 0) {

  var description: BookDescription = new BookDescription
  var reviews: BookReviews = new BookReviews


  def copy: Aggregate = {
    BeanUtil.copyProperties(new Book, this)
  }
}
