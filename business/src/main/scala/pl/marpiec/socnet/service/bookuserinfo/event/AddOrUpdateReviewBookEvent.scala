package pl.marpiec.socnet.service.bookuserinfo.event

import pl.marpiec.socnet.constant.Rating
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.bookuserinfo.BookReview
import org.joda.time.LocalDateTime
import pl.marpiec.socnet.model.BookUserInfo

/**
 * @author Marcin Pieciukiewicz
 */

class AddOrUpdateReviewBookEvent(val reviewerUserId: UID, val description: String, val rating: Rating, val reviewTime: LocalDateTime) extends Event {
  def entityClass = classOf[BookUserInfo]

  def applyEvent(aggregate: Aggregate) {
    val bookUserInfo = aggregate.asInstanceOf[BookUserInfo]

    val review = new BookReview
    review.description = description
    review.rating = rating
    review.userId = reviewerUserId
    review.creationTime = reviewTime

    bookUserInfo.reviewOption = Option(review)

  }
}
