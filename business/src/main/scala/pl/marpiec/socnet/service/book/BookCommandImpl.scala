package pl.marpiec.socnet.service.book

import event.CreateBookEvent
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{EventRow, EventStore}
import pl.marpiec.socnet.model.book.BookDescription

/**
 * @author Marcin Pieciukiewicz
 */

@Service("bookCommand")
class BookCommandImpl @Autowired()(val eventStore: EventStore) extends BookCommand {

  def createBook(userId: UID, bookDescription: BookDescription, newBookId: UID) {
    val createBook = new CreateBookEvent(bookDescription)
    eventStore.addEventForNewAggregate(newBookId, new EventRow(userId, newBookId, 0, createBook))
  }
}
