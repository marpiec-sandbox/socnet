package pl.marpiec.socnet.service.bookuserinfo.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.{UID, BeanUtil}
import pl.marpiec.socnet.model.bookuserinfo.BookOwnership
import pl.marpiec.socnet.service.bookuserinfo.input.BookOwnershipInput
import pl.marpiec.socnet.model.BookUserInfo

/**
 * @author Marcin Pieciukiewicz
 */

class AddOrUpdateBookOwnershipEvent(val userId: UID, val bookOwnershipInput: BookOwnershipInput) extends Event {
  def entityClass = classOf[BookUserInfo]

  def applyEvent(aggregate: Aggregate) {
    val bookUserInfo = aggregate.asInstanceOf[BookUserInfo]

    val bookOwnership = new BookOwnership
    BeanUtil.copyProperties(bookOwnership, bookOwnershipInput)

    bookUserInfo.ownershipOption = Option(bookOwnership)
  }
}
