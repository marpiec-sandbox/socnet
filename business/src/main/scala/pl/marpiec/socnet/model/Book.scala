package pl.marpiec.socnet.model

import book.{BookReviews, BookDescription}
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.BeanUtil

/**
 * @author Marcin Pieciukiewicz
 */

class Book extends Aggregate(null, 0) {

  val description: BookDescription = new BookDescription
  val reviews: BookReviews = new BookReviews


  def copy: Aggregate = {
    BeanUtil.copyProperties(new Article, this)
  }
}
