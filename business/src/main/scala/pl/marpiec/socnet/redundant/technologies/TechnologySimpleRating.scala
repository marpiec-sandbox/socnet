package pl.marpiec.socnet.redundant.technologies

/**
 * @author Marcin Pieciukiewicz
 */

object TechnologySimpleRating {
  def apply(technologyName:String) = new TechnologySimpleRating(technologyName, 0, 0)
}

class TechnologySimpleRating(val technologyName:String, val votesCount: Int, val ratingSum: Int)
  extends Comparable[TechnologySimpleRating]{

  val averageRating = {
    if (votesCount == 0) {
      0.0
    } else {
      (ratingSum.toDouble / votesCount.toDouble)
    }
  }

  def addVote(rating: Int) = new TechnologySimpleRating(technologyName, votesCount + 1, ratingSum + rating)

  def removeVote(rating: Int) = new TechnologySimpleRating(technologyName, votesCount - 1, ratingSum - rating)

  def compareTo(rating: TechnologySimpleRating) = {
    if(this.averageRating < rating.averageRating) {
      -1
    } else if (this.averageRating > rating.averageRating) {
      1
    } else {
      0
    }
  }
}