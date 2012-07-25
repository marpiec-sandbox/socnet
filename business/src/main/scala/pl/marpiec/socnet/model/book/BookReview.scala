package pl.marpiec.socnet.model.book

import pl.marpiec.socnet.constant.Rating
import pl.marpiec.util.UID
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class BookReview {

  var creationTime: LocalDateTime = _
  var userId: UID = _
  var rating: Rating = _
  var description: String = _

}
