package pl.marpiec.socnet.model.book

import pl.marpiec.socnet.constant.Rating

/**
 * @author Marcin Pieciukiewicz
 */

class BookReviews {

  var reviews:List[BookReview] = Nil
  var votes:Map[Rating, Int] = Map()
  
}
