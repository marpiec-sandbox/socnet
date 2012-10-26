package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.readdatabase.BookDatabase
import org.apache.commons.lang.StringUtils
import pl.marpiec.util.{StringFormattingUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */


class BookDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[Book](dataStore) with BookDatabase {


  startListeningToDataStore(dataStore, classOf[Book])

  def getBookById(id: UID): Option[Book] = getById(id)

  def getAllBooks: List[Book] = getAll

  def getBooksByIds(booksIds: List[UID]): List[Book] = {
    getAllBooks.filter(book => {
      booksIds.contains(book.id)
    })
  }

  def findBooksByQuery(query: String): List[Book] = {

    val lowerCaseQuery = StringFormattingUtil.toLowerCase(query)

    getAllBooks.asInstanceOf[List[Book]].filter(book => {
      if(StringUtils.containsIgnoreCase(book.description.title, lowerCaseQuery) ||
        StringUtils.containsIgnoreCase(book.description.polishTitle, lowerCaseQuery) ||
        StringUtils.containsIgnoreCase(book.description.description, lowerCaseQuery) ||
        StringUtils.containsIgnoreCase(book.description.isbn, lowerCaseQuery)){
        true
      } else {
        book.description.authors.exists(author => {
          StringUtils.containsIgnoreCase(author, lowerCaseQuery)
        })
      }
    })
  }
  
}
