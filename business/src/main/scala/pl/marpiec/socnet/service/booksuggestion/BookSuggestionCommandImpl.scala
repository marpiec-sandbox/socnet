package pl.marpiec.socnet.service.booksuggestion

import event.CreateBookSuggestionEvent
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.{EventRow, EventStore}

/**
 * @author Marcin Pieciukiewicz
 */

@Service("bookCommand")
class BookSuggestionCommandImpl @Autowired()(val eventStore: EventStore) extends BookSuggestionCommand {

  def createBookSuggestion(userId: UID, bookDescription: BookDescription, userComment: String,
                           creationTime: LocalDateTime, newBookSuggestionId: UID) {

    val createBookSuggestion = new CreateBookSuggestionEvent(bookDescription, userComment, userId, creationTime)
    eventStore.addEventForNewAggregate(newBookSuggestionId, new EventRow(userId, newBookSuggestionId, 0, createBookSuggestion))
  }

}
