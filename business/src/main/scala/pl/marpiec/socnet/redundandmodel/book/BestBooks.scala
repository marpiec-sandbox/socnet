package pl.marpiec.socnet.redundandmodel.book

import pl.marpiec.util.UID
import pl.marpiec.socnet.constant.Rating



class BestBooks {

  val ZERO_BOOK_RATING = new BookSimpleRating(new UID(0), 0, 0)
  var booksRatings: Map[UID, BookSimpleRating] = Map[UID, BookSimpleRating]()
  var bestBooks: List[BookSimpleRating] = List[BookSimpleRating]()

  def addVote(bookId: UID, rating: Rating) {
    val oldBookRating = getNewBookRatingCopy(bookId)

    val newBookRating = new BookSimpleRating(bookId,
      oldBookRating.ratingSum + rating.numericValue, oldBookRating.votesCount + 1)

    putBookRatingInMapAndList(newBookRating)
  }

  def changeVote(bookId: UID, rating: Rating, previousRating: Rating) {
    val oldBookRating = getNewBookRatingCopy(bookId)

    val newBookRating = new BookSimpleRating(bookId,
      oldBookRating.ratingSum + rating.numericValue - previousRating.numericValue,
      oldBookRating.votesCount)

    putBookRatingInMapAndList(newBookRating)
  }

  def removeVote(bookId: UID, rating: Rating) {
    val oldBookRating = getNewBookRatingCopy(bookId)

    val newBookRating = new BookSimpleRating(bookId,
      oldBookRating.ratingSum - rating.numericValue,
      oldBookRating.votesCount - 1)

    putBookRatingInMapAndList(newBookRating)
  }

  def putBookRatingInMapAndList(bookRating:BookSimpleRating) {
    booksRatings += bookRating.bookId -> bookRating
    val filtered = bestBooks.filterNot(rating => rating.bookId == bookRating.bookId)
    var resultList = List[BookSimpleRating]()
    var added = false

    if (filtered.isEmpty) {
      resultList ::= bookRating
    } else {
      filtered.foreach(rating => {
        if (rating.averageRating < bookRating.averageRating && !added) {
          resultList ::= bookRating
          added = true
        }
        resultList ::= rating
      })
    }
    bestBooks = resultList
  }

  def getBestBooks(count:Int):List[BookSimpleRating] = {
    bestBooks.take(count)
  }

  private def getNewBookRatingCopy(bookId: UID): BookSimpleRating = {
    val bookRatingOption = booksRatings.get(bookId)
    if (bookRatingOption.isDefined) {
      bookRatingOption.get
    } else {
      ZERO_BOOK_RATING
    }
  }

  def copy:BestBooks = {
    val bestBooks = new BestBooks
    bestBooks.booksRatings = this.booksRatings
    bestBooks.bestBooks = this.bestBooks
    bestBooks
  }
}
