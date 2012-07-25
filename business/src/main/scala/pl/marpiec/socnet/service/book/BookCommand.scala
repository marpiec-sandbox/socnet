package pl.marpiec.socnet.service.book

import input.BookOwnershipInput
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.book.BookDescription
import pl.marpiec.socnet.constant.Rating

/**
 * @author Marcin Pieciukiewicz
 */

trait BookCommand {

  def createBook(userId: UID, bookDescription: BookDescription, newBookId: UID)
  def changeBookDescription(userId: UID, id: UID, version: Int, bookDescription: BookDescription)

  def voteForBook(userId: UID, id: UID, version: Int, rating: Rating)
  def addOrUpdateReview(userId: UID, id: UID, version: Int, description: String, rating: Rating)

  def addOrUpdateBookOwnership(userId: UID, id: UID, version: Int, input: BookOwnershipInput)

}
