package pl.marpiec.socnet.readdatabase.mock

import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.redundandmodel.book.{BestBooks, BookSimpleRating, BookReviews}
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
  val bestBooks = new BestBooks


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
    updateBestBooks(bookUserInfo, bookReviews) //must be before updating user votes
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

  private def updateBestBooks(bookUserInfo: BookUserInfo, bookReviews: BookReviews) {
    val previousVoteOption = bookReviews.userVotes.get(bookUserInfo.userId)
    if (previousVoteOption.isDefined) {
      if (bookUserInfo.voteOption.isDefined) {
        bestBooks.changeVote(bookUserInfo.bookId, bookUserInfo.voteOption.get, previousVoteOption.get)
      } else {
        bestBooks.removeVote(bookUserInfo.bookId, previousVoteOption.get)
      }
    } else {
      if (bookUserInfo.voteOption.isDefined) {
        bestBooks.addVote(bookUserInfo.bookId, bookUserInfo.voteOption.get)
      }
    }
  }

  def getBooksReviewsForBooksIds(booksIds: List[UID]) = {
    booksReviews.filterKeys(bookId => booksIds.contains(bookId))
  }

  def getBestBooks(count: Int):List[BookSimpleRating] = {
    bestBooks.getBestBooks(count)
  }
}
