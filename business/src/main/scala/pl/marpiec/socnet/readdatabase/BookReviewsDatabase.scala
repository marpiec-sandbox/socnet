package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.redundandmodel.book.BookReviews

/**
 * @author Marcin Pieciukiewicz
 */

trait BookReviewsDatabase {

  def getBookReviews(bookId: UID): Option[BookReviews]
  def getBooksReviewsForBooksIds(booksIds: List[UID]):Map[UID, BookReviews]

}
