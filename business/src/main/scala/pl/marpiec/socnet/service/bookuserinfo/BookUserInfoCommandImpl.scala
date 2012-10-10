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

  def createAndGetNewBookUserInfo(userId:UID, bookId:UID):BookUserInfo = {
    val bookUserInfoId = uidGenerator.nextUid
    createBookUserInfo(userId, bookId, bookUserInfoId)
    
    val event = new CreateBookUserInfoEvent(userId, bookId)
    val bookUserInfo = new BookUserInfo
    bookUserInfo.id = bookUserInfoId
    event.applyEvent(bookUserInfo)
    bookUserInfo
  }
  
  def createBookUserInfo(userId: UID, bookId: UID, bookUserInfoId: UID) {
    val createBook = new CreateBookUserInfoEvent(userId, bookId)
    eventStore.addEventForNewAggregate(bookUserInfoId, new EventRow(userId, bookUserInfoId, 0, createBook))
  }

  def voteForBook(userId: UID, id: UID, version: Int, rating: Rating) {
    eventStore.addEvent(new EventRow(userId, id, version, new VoteForBookEvent(userId, rating)))
  }

  def addOrUpdateReview(userId: UID, id: UID, version: Int, description: String, rating: Rating, reviewTime: LocalDateTime) {
    eventStore.addEvent(new EventRow(userId, id, version, new AddOrUpdateReviewBookEvent(userId, description, rating, reviewTime)))
  }

  def addOrUpdateBookOwnership(userId: UID, id: UID, version: Int, bookOwnershipInput: BookOwnershipInput) {
    eventStore.addEventIgnoreVersion(new EventRow(userId, id, version, new AddOrUpdateBookOwnershipEvent(userId, bookOwnershipInput)))
  }
}
