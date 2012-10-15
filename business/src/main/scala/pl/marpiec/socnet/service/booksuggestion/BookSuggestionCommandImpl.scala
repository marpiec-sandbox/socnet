package pl.marpiec.socnet.service.booksuggestion

import event._
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.{EventRow, EventStore}

/**
 * @author Marcin Pieciukiewicz
 */

@Service("bookSuggestionCommand")
class BookSuggestionCommandImpl @Autowired()(val eventStore: EventStore) extends BookSuggestionCommand {

  def createBookSuggestion(userId: UID, bookDescription: BookDescription, userComment: String,
                           creationTime: LocalDateTime, newBookSuggestionId: UID) {

    val createBookSuggestion = new CreateBookSuggestionEvent(bookDescription, userComment, userId, creationTime)
    eventStore.addEventForNewAggregate(newBookSuggestionId, new EventRow(userId, newBookSuggestionId, 0, createBookSuggestion))
  }

  def acceptBookSuggestion(userId: UID, id: UID, version: Int, createdBookId: UID, responseTime: LocalDateTime) {
    eventStore.addEvent(new EventRow(userId, id, version, new AcceptBookSuggestionEvent(createdBookId, responseTime)))
  }

  def bookAlreadyExistsForBookSuggestion(userId: UID, id: UID, version: Int, existingBookId: UID, responseTime: LocalDateTime) {
    eventStore.addEvent(new EventRow(userId, id, version, new BookAlreadyExistedForSuggestionEvent(existingBookId, responseTime)))
  }

  def declineBookSuggestion(userId: UID, id: UID, version: Int, comment: String, responseTime: LocalDateTime) {
    eventStore.addEvent(new EventRow(userId, id, version, new DeclineBookSuggestionEvent(comment, responseTime)))
  }

  def userHasSeenResponse(userId: UID, id: UID, version: Int) {
    eventStore.addEvent(new EventRow(userId, id, version, new UserHasSeenSuggestionResponseEvent))
  }

  def removeSuggestionFromUserList(userId: UID, id: UID, version: Int) {
    eventStore.addEvent(new EventRow(userId, id, version, new RemoveSuggestionFromUserListEvent))
  }
}
