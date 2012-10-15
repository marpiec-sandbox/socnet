package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.BookSuggestion

/**
 * @author Marcin Pieciukiewicz
 */

trait BookSuggestionDatabase {

  def getBookSuggestionById(id: UID): Option[BookSuggestion]

  def getAllUnrespondedSuggestions: List[BookSuggestion]
}
