package pl.marpiec.socnet.service.booksuggestion.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.BookSuggestion


/**
 * @author Marcin Pieciukiewicz
 */

class RemoveSuggestionFromUserListEvent extends Event {

  def applyEvent(aggregate: Aggregate) {
    val bookSuggestion = aggregate.asInstanceOf[BookSuggestion]
    bookSuggestion.removedFromUsersList = true
  }

  def entityClass = classOf[BookSuggestion]
}
