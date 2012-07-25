package pl.marpiec.socnet.model.book

import pl.marpiec.socnet.constant.Rating

/**
 * @author Marcin Pieciukiewicz
 */

class BookReviews {

  var userReviews:List[BookReview] = Nil
  var votes:Map[Rating, Int] = Map() + (Rating.ONE -> 0) +
                                       (Rating.TWO -> 0) +
                                       (Rating.THREE -> 0) +
                                       (Rating.FOUR -> 0) +
                                       (Rating.FIVE -> 0)
  
}
