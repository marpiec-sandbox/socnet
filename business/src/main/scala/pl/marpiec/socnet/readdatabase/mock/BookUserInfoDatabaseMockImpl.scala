package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.BookUserInfoDatabase
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.model.BookUserInfo
import org.springframework.stereotype.Repository

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("bookUserInfoDatabase")
class BookUserInfoDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[BookUserInfo](dataStore) with BookUserInfoDatabase {

  val USERBOOK_INDEX: String = "userbook"

  startListeningToDataStore(dataStore, classOf[BookUserInfo])

  addIndex(USERBOOK_INDEX, (aggregate: Aggregate) => {
    val bookUserInfo = aggregate.asInstanceOf[BookUserInfo]
    bookUserInfo.bookId.toString.concat("-".concat(bookUserInfo.userId.toString))
  });

  def getUserInfoById(id: UID): Option[BookUserInfo] = getById(id)

  def getUserInfoByUserAndBook(userId: UID, bookId: UID): Option[BookUserInfo] =
    getByIndex(USERBOOK_INDEX, bookId.toString.concat("-".concat(userId.toString)))

  def getBooksOwnedBy(userId: UID): List[UID] = {
    
    var books = List[UID]()
    getAll.foreach(bookUserInfo => {
      if(bookUserInfo.userId == userId && 
        bookUserInfo.ownershipOption.isDefined &&
        bookUserInfo.ownershipOption.get.isInterestedInBook) {
        books ::= bookUserInfo.bookId
      }
    })

    books
  }

  def getUserInfoForBooks(userId: UID, booksIds: List[UID]) = {
    var booksInfos = Map[UID, BookUserInfo]()
    getAll.foreach(bookUserInfo => {
      if(bookUserInfo.userId == userId) {
        booksInfos += bookUserInfo.bookId -> bookUserInfo
      }
    })
    booksInfos
  }
}
