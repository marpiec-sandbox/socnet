package pl.marpiec.socnet.readdatabase.nosql

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.BookDatabase
import pl.marpiec.cqrs.{Aggregate, DataStoreListener, DataStore}
import pl.marpiec.util.UID
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import com.mongodb.QueryBuilder
import pl.marpiec.socnet.model.Book
import org.springframework.stereotype.Repository

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("bookDatabase")
class BookDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener with BookDatabase {

  val connector = new DatabaseConnectorImpl("books")

  startListeningToDataStore(dataStore, classOf[Book])

  def getBookByTitle(title: String) = {
    connector.findAggregateByQuery(QueryBuilder.start("title").is(title).get(), classOf[Book])
  }

  def getBookById(id: UID) = connector.getAggregateById(id, classOf[Book])

  def getAllBooks = connector.getAllAggregates(classOf[Book])

  def onEntityChanged(entity: Aggregate) {
    connector.insertAggregate(entity.asInstanceOf[Book])
  }

  def getBooksOwnedBy(userId: UID):List[Book] = {
    val allBooks:List[Book] = connector.findMultipleAggregatesByQuery(QueryBuilder.start("ownership.k").is(userId.uid).get, classOf[Book])
    allBooks.filter(book => {
      book.ownership.get(userId).get.isInterestedInBook
    })
    
  }
}
