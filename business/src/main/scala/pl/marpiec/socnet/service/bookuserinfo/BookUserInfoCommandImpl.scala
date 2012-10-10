package pl.marpiec.socnet.service.bookuserinfo

import event.{CreateBookUserInfoEvent, AddOrUpdateBookOwnershipEvent, AddOrUpdateReviewBookEvent, VoteForBookEvent}
import input.BookOwnershipInput
import org.springframework.stereotype.Service
import pl.marpiec.util.UID
import pl.marpiec.socnet.constant.Rating
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.model.BookUserInfo
import pl.marpiec.cqrs.{UidGenerator, UidGeneratorDbImpl, EventStore, EventRow}

/**
 * @author Marcin Pieciukiewicz
 */

@Service("bookUserInfoCommand")
class BookUserInfoCommandImpl @Autowired()(val eventStore: EventStore, uidGenerator: UidGenerator) extends BookUserInfoCommand {
  
  def createBookUserInfo(userId: UID, bookId: UID, bookUserInfoId: UID) {
    val createBook = new CreateBookUserInfoEvent(userId, bookId)
    eventStore.addEventForNewAggregate(bookUserInfoId, new EventRow(userId, bookUserInfoId, 0, createBook))
  }

  def voteForBook(userId: UID, bookId: UID, bookUserInfo: BookUserInfo, rating: Rating) {

    createBookUserInfoIfRequired(userId, bookId, bookUserInfo)
    eventStore.addEvent(new EventRow(userId, bookUserInfo.id, bookUserInfo.version, new VoteForBookEvent(userId, rating)))
  }

  def addOrUpdateReview(userId: UID, bookId: UID, bookUserInfo: BookUserInfo, description: String, rating: Rating, reviewTime: LocalDateTime) {
    createBookUserInfoIfRequired(userId, bookId, bookUserInfo)
    eventStore.addEvent(new EventRow(userId, bookUserInfo.id, bookUserInfo.version, new AddOrUpdateReviewBookEvent(userId, description, rating, reviewTime)))
  }

  def addOrUpdateBookOwnership(userId: UID, bookId: UID, bookUserInfo: BookUserInfo, bookOwnershipInput: BookOwnershipInput) {
    createBookUserInfoIfRequired(userId, bookId, bookUserInfo)
    eventStore.addEvent(new EventRow(userId, bookUserInfo.id, bookUserInfo.version, new AddOrUpdateBookOwnershipEvent(userId, bookOwnershipInput)))
  }

  private def createBookUserInfoIfRequired(userId: UID, bookId: UID, bookUserInfo: BookUserInfo) {

    if(bookUserInfo.id == null && bookUserInfo.version == 0) {
      val bookUserInfoId = uidGenerator.nextUid
      createBookUserInfo(userId, bookId, bookUserInfoId)
      val event = new CreateBookUserInfoEvent(userId, bookId)
      bookUserInfo.id = bookUserInfoId
      bookUserInfo.version = 1
      event.applyEvent(bookUserInfo)
    }

  }
}
