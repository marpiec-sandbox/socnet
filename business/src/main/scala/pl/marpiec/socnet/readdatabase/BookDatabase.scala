package pl.marpiec.socnet.readdatabase

import pl.marpiec.socnet.model.Book
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait BookDatabase {

  def getBookByTitle(title: String): Option[Book]
  def getBookById(id: UID): Option[Book]
  def getAllBooks: List[Book]
  def getBooksByIds(booksIds: List[UID]): List[Book]

}
