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

  def createAndGetNewBookUserInfo(userId: UID, bookId: UID): BookUserInfo

  def createBookUserInfo(userId: UID, bookId: UID, bookUserInfoId: UID)

  def voteForBook(userId: UID, id: UID, version: Int, rating: Rating)

  def addOrUpdateReview(userId: UID, id: UID, version: Int, description: String, rating: Rating, review: LocalDateTime)

  def addOrUpdateBookOwnership(userId: UID, id: UID, version: Int, input: BookOwnershipInput)

}
