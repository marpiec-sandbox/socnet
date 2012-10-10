package pl.marpiec.socnet.service.bookuserinfo.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.BookUserInfo
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateBookUserInfoEvent(userId:UID, bookId:UID) extends Event {

  def entityClass = classOf[BookUserInfo]

  def applyEvent(aggregate: Aggregate) {
    val bookUserInfo = aggregate.asInstanceOf[BookUserInfo]
    bookUserInfo.userId = userId
    bookUserInfo.bookId = bookId
  }
}