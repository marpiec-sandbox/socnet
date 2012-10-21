package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.{DataStoreListener, DataStore}
import pl.marpiec.socnet.model.{Book, UserContacts}
import pl.marpiec.socnet.readdatabase.{BestBooksDatabase, UserContactsDatabase}
import pl.marpiec.socnet.redundandmodel.book.BestBooks

/**
 * @author Marcin Pieciukiewicz
 */
class BestBooksDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends BestBooksDatabase with DataStoreListener[Book] {

  val bestBooks:BestBooks = new BestBooks

  def getBestBooks =

  def onEntityChanged(entity: Book) {}
}
