package pl.marpiec.socnet.service.book.event

import pl.marpiec.socnet.constant.Rating
import pl.marpiec.socnet.model.Book
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class VoteForBookEvent(val userId:UID, val rating:Rating) extends Event {
  def entityClass = classOf[Book]

  def applyEvent(aggregate: Aggregate) {
    val book = aggregate.asInstanceOf[Book]
    
    val previousUserVoteOption:Option[Rating] = book.reviews.userVotes.get(userId)

    //if user voted before, remove previous rating
    if (previousUserVoteOption.isDefined) {
      val previousUserVote = previousUserVoteOption.get
      val currentVotes = book.reviews.votes(previousUserVote)
      book.reviews.votes += previousUserVote -> (currentVotes-1)
    }
    
    val currentVotes = book.reviews.votes(rating)
    book.reviews.votes += rating -> (currentVotes+1)
    
    book.reviews.userVotes += userId -> rating
  }
}
