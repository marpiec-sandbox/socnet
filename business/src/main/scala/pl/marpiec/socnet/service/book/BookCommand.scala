package pl.marpiec.socnet.service.book

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

trait BookCommand {

  def createBook(userId: UID, bookDescription: BookDescription, creationTime: LocalDateTime, newBookId: UID)

  def changeBookDescription(userId: UID, id: UID, version: Int, bookDescription: BookDescription)

}
