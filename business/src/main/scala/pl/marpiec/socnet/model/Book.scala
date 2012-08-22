package pl.marpiec.socnet.model

import book.{BookOwnership, BookReviews, BookDescription}
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{UID, BeanUtil}
import org.joda.time.LocalDateTime
import pl.marpiec.socnet.constant.Rating

/**
 * @author Marcin Pieciukiewicz
 */

class Book extends Aggregate(null, 0) {

  var creationTime: LocalDateTime = _
  var description = new BookDescription
  var reviews = new BookReviews
  var ownership: Map[UID, BookOwnership] = Map()


  def copy: Aggregate = {
    BeanUtil.copyProperties(new Book, this)
  }


  def getUserRating(userId: UID): Option[Rating] = {
    reviews.userVotes.get(userId)
  }


  def getAverageRating: Double = {
    var (sum, votesCount) = calculateVotesSum
    if (votesCount == 0) {
      0.0
    } else {
      sum.toDouble / votesCount.toDouble
    }

  }

  def calculateVotesSum: (Int, Int) = {
    var sum = 0
    var votesCount = 0
    for ((rating, votes) <- reviews.votes) {
      sum += rating.numericValue * votes
      votesCount += votes
    }
    (sum, votesCount)
  }

  def getAverageRatingWithOneVoteChanged(userId: UID, rating: Rating) = {
    var (sum, votesCount) = calculateVotesSum
    var previousUserRatingOption = reviews.userVotes.get(userId)
    if(previousUserRatingOption.isDefined) {
      sum -= previousUserRatingOption.get.numericValue
      sum += rating.numericValue
    } else {
      votesCount += 1
      sum += rating.numericValue
    }
    sum.toDouble / votesCount.toDouble
  }


  def getFormattedAverageRating: String = {
    "%1.0f".format(getAverageRating)
  }

  def getFormattedAverageRatingWithOneVoteChanged(userId: UID, rating: Rating): String = {
    "%1.0f".format(getAverageRatingWithOneVoteChanged(userId, rating))
  }

}
