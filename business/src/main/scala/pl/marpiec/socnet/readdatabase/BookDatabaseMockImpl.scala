package pl.marpiec.socnet.readdatabase

import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("bookDatabase")
class BookDatabaseMockImpl @Autowired()(dataStore: DataStore) extends AbstractDatabase[Book](dataStore) with BookDatabase {

  val TITLE_INDEX: String = "title"

  startListeningToDataStore(dataStore, classOf[Book])

  addIndex(TITLE_INDEX, (aggregate: Aggregate) => {
    val book = aggregate.asInstanceOf[Book]
    book.description.title
  });

  def getBookByTitle(title: String): Option[Book] = getByIndex(TITLE_INDEX, title)

  def getBookById(id: UID): Option[Book] = getById(id)

  def getAllBooks: List[Book] = getAll
}
