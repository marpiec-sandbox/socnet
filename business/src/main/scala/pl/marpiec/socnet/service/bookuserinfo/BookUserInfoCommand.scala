package pl.marpiec.socnet.service.bookuserinfo

import input.BookOwnershipInput
import pl.marpiec.util.UID
import pl.marpiec.socnet.constant.Rating
import org.joda.time.LocalDateTime
import pl.marpiec.socnet.model.BookUserInfo

/**
 * @author Marcin Pieciukiewicz
 */

trait BookUserInfoCommand {

  def voteForBook(userId: UID, bookId: UID, bookUserInfo: BookUserInfo, rating: Rating)

  def cancelVoteForBook(userId: UID, bookId: UID, bookUserInfo: BookUserInfo)

  def addOrUpdateReview(userId: UID, bookId: UID, bookUserInfo: BookUserInfo, description: String, rating: Rating, review: LocalDateTime)

  def removeBookReview(userId: UID, id: UID, version: Int)

  def addOrUpdateBookOwnership(userId: UID, bookId: UID, bookUserInfo: BookUserInfo, input: BookOwnershipInput)

}
