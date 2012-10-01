package pl.marpiec.socnet.model.book

import pl.marpiec.socnet.constant.Rating
import pl.marpiec.util.UID
import pl.marpiec.util.mpjson.annotation.SecondSubType

/**
 * @author Marcin Pieciukiewicz
 */

class BookReviews {

  var userReviews:List[BookReview] = Nil
  @SecondSubType(classOf[Int])
  var votes:Map[Rating, Int] = Map() + (Rating.ONE -> 0) +
                                       (Rating.TWO -> 0) +
                                       (Rating.THREE -> 0) +
                                       (Rating.FOUR -> 0) +
                                       (Rating.FIVE -> 0)
  
  var userVotes:Map[UID, Rating] = Map()


}
