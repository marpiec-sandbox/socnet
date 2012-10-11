package pl.marpiec.socnet.service.bookuserinfo.event

import pl.marpiec.socnet.model.BookUserInfo
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * @author Marcin Pieciukiewicz
 */

class RemoveBookReviewEvent extends Event {

  def entityClass = classOf[BookUserInfo]

  def applyEvent(aggregate: Aggregate) {
    val bookUserInfo = aggregate.asInstanceOf[BookUserInfo]
    bookUserInfo.reviewOption = None
  }

}
