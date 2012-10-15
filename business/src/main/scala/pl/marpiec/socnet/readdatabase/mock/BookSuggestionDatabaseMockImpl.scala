package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.cqrs.DataStore
import pl.marpiec.socnet.readdatabase.BookSuggestionDatabase
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.BookSuggestion
import org.springframework.stereotype.Repository

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("bookSuggestionDatabase")
class BookSuggestionDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[BookSuggestion](dataStore) with BookSuggestionDatabase {

  startListeningToDataStore(dataStore, classOf[BookSuggestion])

  def getBookSuggestionById(id: UID): Option[BookSuggestion] = getById(id)

  def getAllUnrespondedSuggestions = getAll.filter(suggestion => suggestion.responseOption.isEmpty)
}
