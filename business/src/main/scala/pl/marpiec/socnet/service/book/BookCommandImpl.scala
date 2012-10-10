package pl.marpiec.socnet.service.book

import event._
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{EventRow, EventStore}
import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

@Service("bookCommand")
class BookCommandImpl @Autowired()(val eventStore: EventStore) extends BookCommand {

  def createBook(userId: UID, bookDescription: BookDescription, creationTime: LocalDateTime, newBookId: UID) {
    val createBook = new CreateBookEvent(bookDescription, creationTime)
    eventStore.addEventForNewAggregate(newBookId, new EventRow(userId, newBookId, 0, createBook))
  }

  def changeBookDescription(userId: UID, id: UID, version: Int, bookDescription: BookDescription) {
    eventStore.addEvent(new EventRow(userId, id, version, new ChangeBookDescriptionEvent(bookDescription)))
  }

}
