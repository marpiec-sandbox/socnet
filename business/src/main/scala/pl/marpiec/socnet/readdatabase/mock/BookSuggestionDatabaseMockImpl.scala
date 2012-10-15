package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.BookSuggestionDatabase
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.model.{UserProfile, BookSuggestion}

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("bookSuggestionDatabase")
class BookSuggestionDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[BookSuggestion](dataStore) with BookSuggestionDatabase {

  startListeningToDataStore(dataStore, classOf[BookSuggestion])

  def getBookSuggestionById(id: UID): Option[BookSuggestion] = getById(id)

  def getAllUnrespondedSuggestions = getAll.filter(suggestion => suggestion.responseOption.isEmpty)

  def getBooksSuggestionsOfUser(userId: UID) = getAll.filter(suggestion => {
    suggestion.userId == userId && !suggestion.removedFromUsersList
  })
}
