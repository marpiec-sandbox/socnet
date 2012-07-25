package pl.marpiec.socnet.service.book.event

import pl.marpiec.socnet.constant.Rating
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.book.BookReview
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class AddOrUpdateReviewBookEvent(val reviewerUserId: UID, val description: String, val rating: Rating, reviewTime: LocalDateTime) extends Event {
  def entityClass = classOf[Book]

  def applyEvent(aggregate: Aggregate) {
    val book = aggregate.asInstanceOf[Book]

    val reviewOption = book.reviews.userReviews.find(userReview => userReview.userId == reviewerUserId)

    val review = reviewOption.getOrElse({
      val newReview = new BookReview
      book.reviews.userReviews ::= newReview
      newReview
    })

    review.description = description
    review.rating = rating
    review.userId = reviewerUserId
    review.creationTime = reviewTime

  }
}
