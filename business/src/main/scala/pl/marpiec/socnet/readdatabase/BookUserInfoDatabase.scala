package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.BookUserInfo

/**
 * @author Marcin Pieciukiewicz
 */

trait BookUserInfoDatabase {

  def getUserInfoById(id: UID): Option[BookUserInfo]

  def getUserInfoByUserAndBook(userId: UID, bookId: UID): Option[BookUserInfo]

  def getBooksOwnedBy(userId: UID): List[UID]

  def getUserInfoForBooks(userId: UID, booksIds: List[UID]): Map[UID, BookUserInfo]

}
