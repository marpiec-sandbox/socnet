package pl.marpiec.socnet.service.booksuggestion

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

trait BookSuggestionCommand {
  def createBookSuggestion(userId: UID, bookDescription: BookDescription, userComment: String,
                           creationTime: LocalDateTime, bookSuggestionId: UID)

}
