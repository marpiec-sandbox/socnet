package pl.marpiec.socnet.redundandmodel.book

import pl.marpiec.util.UID
import pl.marpiec.socnet.constant.Rating

/**
 * @author Marcin Pieciukiewicz
 */

class BookSimpleRating {
  var bookId: UID = _
  var ratingSum: Int = 0
  var votesCount: Int = 0

  def copy:BookSimpleRating = {
    val rating = new BookSimpleRating
    rating.ratingSum = this.ratingSum
    rating.votesCount = this.votesCount
    rating.bookId = this.bookId
    rating
  }

  def averageRating = {
    if (votesCount==0) {
      0.0
    } else {
      ratingSum.toDouble / votesCount.toDouble
    }
  }
}

class BestBooks {

  var booksRatings: Map[UID, BookSimpleRating] = Map[UID, BookSimpleRating]()
  var bestBooks: List[BookSimpleRating] = List[BookSimpleRating]()

  def addVote(bookId: UID, rating: Rating) {
    val bookRating = getNewBookRatingCopy(bookId)
    bookRating.votesCount += 1
    bookRating.ratingSum += rating.numericValue
    putBookRatingInMapAndList(bookRating)
  }

  def changeVote(bookId: UID, rating: Rating, previousRating: Rating) {
    val bookRating = getNewBookRatingCopy(bookId)
    bookRating.ratingSum += (rating.numericValue - previousRating.numericValue)
    putBookRatingInMapAndList(bookRating)
  }

  def putBookRatingInMapAndList(bookRating:BookSimpleRating) {
    booksRatings += bookRating.bookId -> bookRating
    val filtered = bestBooks.filterNot(rating => rating.bookId == bookRating.bookId)
    var resultList = List[BookSimpleRating]()
    var added = false
    filtered.foreach(rating => {
      if (rating.averageRating < bookRating.averageRating && !added) {
        resultList ::= bookRating
      }
      resultList ::= rating
    })
  }

  def getBestBooks(count:Int):List[BookSimpleRating] = {
    bestBooks.take(count)
  }

  private def getNewBookRatingCopy(bookId: UID): BookSimpleRating = {
    val bookRatingOption = booksRatings.get(bookId)
    if (bookRatingOption.isDefined) {
      bookRatingOption.get.copy
    } else {
      val rating = new BookSimpleRating
      rating.bookId = bookId
      rating
    }
  }

  def copy:BestBooks = {
    val bestBooks = new BestBooks
    bestBooks.booksRatings = this.booksRatings
    bestBooks
  }
}
