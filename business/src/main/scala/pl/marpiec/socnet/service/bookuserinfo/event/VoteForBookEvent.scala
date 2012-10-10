package pl.marpiec.socnet.service.bookuserinfo.event

import pl.marpiec.socnet.constant.Rating
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.BookUserInfo

/**
 * @author Marcin Pieciukiewicz
 */

class VoteForBookEvent(val userId: UID, val rating: Rating) extends Event {
  def entityClass = classOf[BookUserInfo]

  def applyEvent(aggregate: Aggregate) {
    val bookUserInfo = aggregate.asInstanceOf[BookUserInfo]
    bookUserInfo.voteOption = Option(rating)
  }
}
