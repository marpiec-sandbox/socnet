package pl.marpiec.socnet.readdatabase.mock

import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.util.UID
import pl.marpiec.socnet.readdatabase.BookDatabase

/**
 * @author Marcin Pieciukiewicz
 */


class BookDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[Book](dataStore) with BookDatabase {

  val TITLE_INDEX: String = "title"

  startListeningToDataStore(dataStore, classOf[Book])

  addIndex(TITLE_INDEX, (aggregate: Aggregate) => {
    val book = aggregate.asInstanceOf[Book]
    book.description.title
  });

  def getBookByTitle(title: String): Option[Book] = getByIndex(TITLE_INDEX, title)

  def getBookById(id: UID): Option[Book] = getById(id)

  def getAllBooks: List[Book] = getAll

  def getBooksOwnedBy(userId: UID):List[Book] = {
    
    var resultList = List[Book]()
    
    getAll.foreach(book => {
      val ownershipOption = book.ownership.get(userId)
      if(ownershipOption.isDefined && ownershipOption.get.isInterestedInBook) {
        resultList ::= book
      }
    })

    resultList
    
  }
}
