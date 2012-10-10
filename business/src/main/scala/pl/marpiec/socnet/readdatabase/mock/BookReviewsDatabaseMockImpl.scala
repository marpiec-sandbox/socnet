package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.redundandmodel.book.BookReviews
import pl.marpiec.socnet.readdatabase.BookReviewsDatabase
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.BookUserInfo
import pl.marpiec.cqrs.{DataStoreListener, DataStore}
import org.springframework.stereotype.Repository

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("bookReviewsDatabase")
class BookReviewsDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener[BookUserInfo] with BookReviewsDatabase {

  var booksReviews = Map[UID, BookReviews]()

  startListeningToDataStore(dataStore, classOf[BookUserInfo])

  def getBookReviews(bookId: UID): Option[BookReviews] = booksReviews.get(bookId)



  def onEntityChanged(bookUserInfo: BookUserInfo) = {

    val bookId = bookUserInfo.bookId

    val bookReviews = booksReviews.getOrElse(bookId, {
      val bookReviews = new BookReviews
      booksReviews += bookId -> bookReviews
      bookReviews
    })
    
    updateBookReviews(bookUserInfo, bookReviews)
    updateUserVotes(bookUserInfo, bookReviews)

  }

  private def updateBookReviews(bookUserInfo: BookUserInfo, bookReviews: BookReviews) {

    //remove old review
    bookReviews.userReviews = bookReviews.userReviews.filterNot(bookReview => {
      bookReview.userId == bookUserInfo.userId
    })

    //add new review
    if (bookUserInfo.reviewOption.isDefined) {
      bookReviews.userReviews ::= bookUserInfo.reviewOption.get
    }
  }
  
  

  private def updateUserVotes(bookUserInfo: BookUserInfo, bookReviews: BookReviews) {

    val previousVoteOption = bookReviews.userVotes.get(bookUserInfo.userId)

    if (bookUserInfo.voteOption.isDefined) {
      bookReviews.userVotes += bookUserInfo.userId -> bookUserInfo.voteOption.get
    } else {
      bookReviews.userVotes -= bookUserInfo.userId
    }
    
    if(previousVoteOption.isDefined) {
      val previousVote = previousVoteOption.get
      bookReviews.votes += previousVote -> (bookReviews.votes(previousVote) - 1)
    }
    
    if (bookUserInfo.voteOption.isDefined) {
      val currentVote = bookUserInfo.voteOption.get
      bookReviews.votes += currentVote -> (bookReviews.votes(currentVote) + 1)
    }
    
  }

  def getBooksReviewsForBooksIds(booksIds: List[UID]) = {
    booksReviews.filterKeys(bookId => booksIds.contains(bookId))
  }
}
