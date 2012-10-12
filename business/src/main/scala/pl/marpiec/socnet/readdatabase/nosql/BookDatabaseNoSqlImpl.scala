package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.BookDatabase
import pl.marpiec.cqrs.{Aggregate, DataStoreListener, DataStore}
import pl.marpiec.util.UID
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import com.mongodb.QueryBuilder
import pl.marpiec.socnet.model.Book
import org.springframework.stereotype.Repository
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("bookDatabase")
class BookDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener[Book] with BookDatabase {

  val connector = new DatabaseConnectorImpl("books")

  startListeningToDataStore(dataStore, classOf[Book])

  def findBooksByQuery(query: String) = {
    //connector.findMultipleAggregatesByQuery(QueryBuilder.start("title").is(title).get(), classOf[Book])

    val lowerCaseQuery = query.toLowerCase

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

  def getBookById(id: UID) = connector.getAggregateById(id, classOf[Book])

  def getAllBooks = connector.getAllAggregates(classOf[Book])

  def onEntityChanged(book: Book) {
    connector.insertAggregate(book)
  }

  def getBooksByIds(booksIds: List[UID]) = {
    
    val ids:Array[Long] = new Array[Long](booksIds.size)

    for (i <- 0 until booksIds.size) {
      ids(i) = booksIds(i).uid
    }

    connector.findMultipleAggregatesByQuery(QueryBuilder.start("_id").in(ids).get, classOf[Book])
  }
}
