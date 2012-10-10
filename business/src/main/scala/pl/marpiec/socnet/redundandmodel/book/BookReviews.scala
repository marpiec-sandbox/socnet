package pl.marpiec.socnet.redundandmodel.book

import pl.marpiec.socnet.constant.Rating
import pl.marpiec.util.mpjson.annotation.SecondSubType
import pl.marpiec.socnet.model.bookuserinfo.BookReview
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class BookReviews {



  var userReviews: List[BookReview] = Nil
  @SecondSubType(classOf[Int])
  var votes: Map[Rating, Int] = Map() + (Rating.ONE -> 0) +
    (Rating.TWO -> 0) +
    (Rating.THREE -> 0) +
    (Rating.FOUR -> 0) +
    (Rating.FIVE -> 0)

  var userVotes: Map[UID, Rating] = Map()


  def getAverageRatingWithOneVoteChanged(userId:UID, rating: Rating) = {
    var (sum, votesCount) = calculateVotesSum
    val previousUserRatingOption = userVotes.get(userId)
    if(previousUserRatingOption.isDefined) {
      sum -= previousUserRatingOption.get.numericValue
      sum += rating.numericValue
    } else {
      votesCount += 1
      sum += rating.numericValue
    }
    sum.toDouble / votesCount.toDouble
  }

  private def getAverageRating: Double = {
    var (sum, votesCount) = calculateVotesSum
    if (votesCount == 0) {
      0.0
    } else {
      sum.toDouble / votesCount.toDouble
    }

  }

  private def calculateVotesSum: (Int, Int) = {
    var sum = 0
    var votesCount = 0
    for ((rating, votes) <- votes) {
      sum += rating.numericValue * votes
      votesCount += votes
    }
    (sum, votesCount)
  }


  def getFormattedAverageRating: String = {
    "%1.1f".format(getAverageRating)
  }

  def getFormattedAverageRatingWithOneVoteChanged(userId:UID, rating: Rating): String = {
    "%1.1f".format(getAverageRatingWithOneVoteChanged(userId, rating))
  }

  def getVotesCount:Int = {
    userVotes.size
  }

  def getVotesCountWithOneVoteChanged(userId:UID):Int = {
    val votesCount = userVotes.size
    val previousUserRatingOption = userVotes.get(userId)
    if(previousUserRatingOption.isDefined) {
      votesCount
    } else {
      votesCount + 1
    }
  }

  def getUserRating(uid: UID) = userVotes.get(uid)


}
