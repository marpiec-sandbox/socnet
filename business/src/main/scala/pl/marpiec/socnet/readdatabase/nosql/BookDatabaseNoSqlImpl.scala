package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.BookDatabase
import pl.marpiec.cqrs.{DataStoreListener, DataStore}
import com.mongodb.QueryBuilder
import pl.marpiec.socnet.model.Book
import org.springframework.stereotype.Repository
import pl.marpiec.socnet.mongodb.{DatabaseTextIndexConnectorImpl, DatabaseConnectorImpl}
import pl.marpiec.socnet.mongodb.model.TextIndex
import pl.marpiec.util.{SearchUtils, UID}

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("bookDatabase")
class BookDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener[Book] with BookDatabase {

  val connector = new DatabaseConnectorImpl("books")
  val connectorBooksIndex = new DatabaseTextIndexConnectorImpl("booksIndex")

  startListeningToDataStore(dataStore, classOf[Book])

  def findBooksByQuery(query: String) = {
    //connector.findMultipleAggregatesByQuery(QueryBuilder.start("title").is(title).get(), classOf[Book])

    val booksIds = connectorBooksIndex.getIdentifierByQuery(SearchUtils.queryToWordsList(query))

    getBooksByIds(booksIds)

  }

  def getBookById(id: UID) = connector.getAggregateById(id, classOf[Book])

  def getAllBooks = connector.getAllAggregates(classOf[Book])

  def onEntityChanged(book: Book) {
    connector.insertAggregate(book)
    connectorBooksIndex.addIndex(new TextIndex(book.id, book.createIndex))
  }

  def getBooksByIds(booksIds: List[UID]) = {

    val ids: Array[Long] = new Array[Long](booksIds.size)

    for (i <- 0 until booksIds.size) {
      ids(i) = booksIds(i).uid
    }

    connector.findMultipleAggregatesByQuery(QueryBuilder.start("_id").in(ids).get, classOf[Book])
  }
}
