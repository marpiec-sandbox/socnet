package pl.marpiec.socnet.redundandmodel.book

import pl.marpiec.util.UID

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
}
