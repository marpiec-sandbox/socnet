package pl.marpiec.socnet.service.book.event

import pl.marpiec.socnet.constant.Rating
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * @author Marcin Pieciukiewicz
 */

class VoteForBookEvent(rating:Rating) extends Event {
  def entityClass = classOf[Book]

  def applyEvent(aggregate: Aggregate) {
    val book = aggregate.asInstanceOf[Book]
    
    val currentVotes = book.reviews.votes(rating)
    book.reviews.votes += rating -> (currentVotes+1)
  }
}
