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

  def acceptBookSuggestion(userId: UID, id: UID, version: Int, createdBookId: UID, responseTime: LocalDateTime)

  def bookAlreadyExistsForBookSuggestion(userId: UID, id: UID, version: Int, existingBookId: UID, responseTime: LocalDateTime)

  def declineBookSuggestion(userId: UID, id: UID, version: Int, comment: String, responseTime: LocalDateTime)

  def userHasSeenResponse(userId: UID, id: UID, version: Int)

  def removeSuggestionFromUserList(userId: UID, id: UID, version: Int)
}
