package pl.marpiec.socnet.redundandmodel.book

import pl.marpiec.util.UID
import pl.marpiec.socnet.constant.Rating

/**
 * @author Marcin Pieciukiewicz
 */

class BookSimpleRating(val bookId: UID, val ratingSum: Int, val votesCount: Int) {

  val averageRating = {
    if (votesCount == 0) {
      0.0
    } else {
      ratingSum.toDouble / votesCount.toDouble
    }
  }

  def changeVote(newBookId:UID, rating: Rating, previousRating: Rating):BookSimpleRating = {
    new BookSimpleRating(newBookId,
      this.ratingSum + rating.numericValue - previousRating.numericValue,
      this.votesCount)
  }

  def removeVote(newBookId:UID, previousRating: Rating):BookSimpleRating = {
    new BookSimpleRating(newBookId,
      this.ratingSum - previousRating.numericValue,
      this.votesCount - 1)
  }

  def addVote(newBookId:UID, rating: Rating):BookSimpleRating = {
    new BookSimpleRating(newBookId,
      this.ratingSum + rating.numericValue,
      this.votesCount + 1)
  }

}
