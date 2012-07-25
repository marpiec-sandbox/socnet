package pl.marpiec.socnet.service.book

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.book.BookDescription

/**
 * @author Marcin Pieciukiewicz
 */

trait BookCommand {
  def createBook(userId: UID, bookDescription: BookDescription, newBookId: UID)
}
