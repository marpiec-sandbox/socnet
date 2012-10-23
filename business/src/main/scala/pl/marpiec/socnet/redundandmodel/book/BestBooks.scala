package pl.marpiec.socnet.redundandmodel.book

import pl.marpiec.util.UID
import pl.marpiec.socnet.constant.Rating

object BestBooks {
  val ZERO_BOOK_RATING = new BookSimpleRating(new UID(0), 0, 0)
}

class BestBooks {

  var bestBooks: List[BookSimpleRating] = List[BookSimpleRating]()

  def addVote(bookId: UID, rating: Rating) {
    synchronized {
      val oldBookRating = getNewBookRatingCopy(bookId)
      val newBookRating = oldBookRating.addVote(bookId, rating)
      putBookRatingInMapAndList(newBookRating)
    }
  }

  def changeVote(bookId: UID, rating: Rating, previousRating: Rating) {
    synchronized {
      val oldBookRating = getNewBookRatingCopy(bookId)
      val newBookRating = oldBookRating.changeVote(bookId, rating, previousRating)
      putBookRatingInMapAndList(newBookRating)
    }
  }

  def removeVote(bookId: UID, previousRating: Rating) {
    synchronized {
      val oldBookRating = getNewBookRatingCopy(bookId)
      val newBookRating = oldBookRating.removeVote(bookId, previousRating)
      putBookRatingInMapAndList(newBookRating)
    }
  }

  def putBookRatingInMapAndList(bookRating:BookSimpleRating) {

    var resultList = List[BookSimpleRating]()
    var added = false

    if (bestBooks.isEmpty || bestBooks.size == 1 && bestBooks.head.bookId == bookRating.bookId) {
      resultList = bookRating :: Nil
      added = true
    } else {
      bestBooks.foreach(rating => {
        if (rating.bookId != bookRating.bookId) {
          if (rating.averageRating < bookRating.averageRating && !added) {
            resultList ::= bookRating
            added = true
          }
          resultList ::= rating
        }
      })

      if (!added){
        resultList ::= bookRating
      }
    }

    bestBooks = resultList.reverse
  }

  def getBestBooks(count:Int):List[BookSimpleRating] = {
    bestBooks.take(count)
  }

  private def getNewBookRatingCopy(bookId: UID): BookSimpleRating = {
    val bookRatingOption = bestBooks.find(bookSimpleRating => bookSimpleRating.bookId == bookId)
    if (bookRatingOption.isDefined) {
      bookRatingOption.get
    } else {
      BestBooks.ZERO_BOOK_RATING
    }
  }

  def copy:BestBooks = {
    val bestBooks = new BestBooks
    bestBooks.bestBooks = this.bestBooks
    bestBooks
  }
}
