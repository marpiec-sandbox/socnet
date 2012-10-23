package pl.marpiec.socnet.service.bookuserinfo.event

import pl.marpiec.socnet.model.BookUserInfo
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Event, Aggregate}

/**
 * @author Marcin Pieciukiewicz
 */

class CancelVoteForBookEvent(val userId: UID) extends Event {
  def entityClass = classOf[BookUserInfo]

  def applyEvent(aggregate: Aggregate) {
    val bookUserInfo = aggregate.asInstanceOf[BookUserInfo]
    bookUserInfo.voteOption = None
  }
}
