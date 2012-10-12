package pl.marpiec.socnet.readdatabase

import pl.marpiec.socnet.model.Book
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait BookDatabase {


  def getBookById(id: UID): Option[Book]
  def getAllBooks: List[Book]
  def getBooksByIds(booksIds: List[UID]): List[Book]

  def findBooksByQuery(query: String): List[Book]

}
