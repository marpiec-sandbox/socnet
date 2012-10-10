package pl.marpiec.socnet.model

import bookuserinfo.{BookReview, BookOwnership}
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.socnet.constant.Rating
import pl.marpiec.util.{BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class BookUserInfo extends Aggregate(null, 0) {

  var userId: UID = _
  var bookId: UID = _
  var voteOption: Option[Rating] = None
  var reviewOption: Option[BookReview] = None
  var ownershipOption: Option[BookOwnership] = None

  def copy = {
    val bookUserInfo = new BookUserInfo
    BeanUtil.copyProperties(bookUserInfo, this)
    bookUserInfo
  }
}
