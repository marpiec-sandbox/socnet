package pl.marpiec.socnet.service.book

import org.testng.annotations.Test

import pl.marpiec.cqrs._
import pl.marpiec.socnet.constant.Rating
import org.joda.time.LocalDateTime
import pl.marpiec.socnet.redundandmodel.book.BookReviews
import pl.marpiec.socnet.model.book.BookDescription
import pl.marpiec.util.UID
import pl.marpiec.socnet.service.bookuserinfo.{BookUserInfoCommandImpl, BookUserInfoCommand}
import pl.marpiec.socnet.readdatabase.{BookReviewsDatabase, BookUserInfoDatabase, BookDatabase}
import pl.marpiec.socnet.service.bookuserinfo.input.BookOwnershipInput
import pl.marpiec.socnet.readdatabase.mock.{BookReviewsDatabaseMockImpl, BookUserInfoDatabaseMockImpl, BookDatabaseMockImpl}
import pl.marpiec.socnet.model.BookUserInfo
import org.testng.Assert._

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class BookServicesTest {

  def testBookCreation() {

    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val bookDatabase: BookDatabase = new BookDatabaseMockImpl(dataStore)
    val bookUserInfoDatabase: BookUserInfoDatabase = new BookUserInfoDatabaseMockImpl(dataStore)
    val bookReviewsDatabase: BookReviewsDatabase = new BookReviewsDatabaseMockImpl(dataStore)

    val bookCommand: BookCommand = new BookCommandImpl(eventStore)
    val bookUserInfoCommand: BookUserInfoCommand = new BookUserInfoCommandImpl(eventStore, uidGenerator)


    /*
     * Create a Book
     */

    val bookDescription = new BookDescription
    bookDescription.title = "Effective Java"
    bookDescription.polishTitle = "Java. Efektywne programowanie"
    bookDescription.authors ::= "Joshua Bloch"
    bookDescription.description = "Ksiazka o programowaniu w jezyku Java"
    bookDescription.isbn = "9788324620845"

    val bookCreatorUserId = uidGenerator.nextUid
    val bookId = uidGenerator.nextUid
    val creationTime = new LocalDateTime(2012, 06, 01, 12, 0, 5)

    bookCommand.createBook(bookCreatorUserId, bookDescription, creationTime, bookId)

    val bookOption = bookDatabase.getBookById(bookId)

    assertTrue(bookOption.isDefined)

    var book = bookOption.get

    assertEquals(book.creationTime, new LocalDateTime(2012, 06, 01, 12, 0, 5))
    assertEquals(book.description.title, "Effective Java")
    assertEquals(book.description.polishTitle, "Java. Efektywne programowanie")
    assertEquals(book.description.authors.size, 1)
    assertEquals(book.description.authors.head, "Joshua Bloch")
    assertEquals(book.description.description, "Ksiazka o programowaniu w jezyku Java")
    assertEquals(book.description.isbn, "9788324620845")


    /*
     * Change book description
     */

    bookDescription.description = "Ksiazka o efektywnym programowaniu w jezyku Java"

    assertNotEquals(book.description.description, bookDescription.description)

    bookCommand.changeBookDescription(bookCreatorUserId, book.id, book.version, bookDescription)

    book = bookDatabase.getBookById(bookId).get

    assertEquals(book.description.title, "Effective Java")
    assertEquals(book.description.polishTitle, "Java. Efektywne programowanie")
    assertEquals(book.description.authors.size, 1)
    assertEquals(book.description.authors.head, "Joshua Bloch")
    assertEquals(book.description.description, "Ksiazka o efektywnym programowaniu w jezyku Java")
    assertEquals(book.description.isbn, "9788324620845")



    /*
     * Vote for a book
     */

    var creatorBookUserInfo = new BookUserInfo

    bookUserInfoCommand.voteForBook(bookCreatorUserId, bookId, creatorBookUserInfo, Rating.FOUR)

    var bookReviews:BookReviews = bookReviewsDatabase.getBookReviews(bookId).get
  
    assertEquals(bookReviews.votes(Rating.ONE), 0)
    assertEquals(bookReviews.votes(Rating.TWO), 0)
    assertEquals(bookReviews.votes(Rating.THREE), 0)
    assertEquals(bookReviews.votes(Rating.FOUR), 1)
    assertEquals(bookReviews.votes(Rating.FIVE), 0)

    creatorBookUserInfo = bookUserInfoDatabase.getUserInfoByUserAndBook(bookCreatorUserId, bookId).get

    assertEquals(creatorBookUserInfo.voteOption.get, Rating.FOUR)

    val bestBooks = bookReviewsDatabase.getBestBooks(5) // 5 is some maximum count of books we want to retrive

    assertEquals(bestBooks.size, 1)
    assertEquals(bestBooks.head.bookId, bookId)
    assertEquals(bestBooks.head.votesCount, 1)
    assertEquals(bestBooks.head.averageRating, 4.0, 0.1)

    bookUserInfoCommand.cancelVoteForBook(bookCreatorUserId, bookId, creatorBookUserInfo)

    bookReviews = bookReviewsDatabase.getBookReviews(bookId).get

    assertEquals(bookReviews.votes(Rating.ONE), 0)
    assertEquals(bookReviews.votes(Rating.TWO), 0)
    assertEquals(bookReviews.votes(Rating.THREE), 0)
    assertEquals(bookReviews.votes(Rating.FOUR), 0)
    assertEquals(bookReviews.votes(Rating.FIVE), 0)

    creatorBookUserInfo = bookUserInfoDatabase.getUserInfoByUserAndBook(bookCreatorUserId, bookId).get

    assertTrue(creatorBookUserInfo.voteOption.isEmpty)

    /*
     * Create book review
     */

    val bookReviewerUserId = uidGenerator.nextUid

    var reviewerBookUserInfo = new BookUserInfo

    val reviewTime = new LocalDateTime(2012, 7, 4, 14, 0, 5)

    bookUserInfoCommand.addOrUpdateReview(bookReviewerUserId, bookId, reviewerBookUserInfo, "Bardzo dobra ksiazka", Rating.FIVE, reviewTime)

    bookReviews = bookReviewsDatabase.getBookReviews(bookId).get

    assertEquals(bookReviews.userReviews.size, 1)
    var review = bookReviews.userReviews.head
    assertEquals(review.creationTime, new LocalDateTime(2012, 7, 4, 14, 0, 5))
    assertEquals(review.description, "Bardzo dobra ksiazka")
    assertEquals(review.rating, Rating.FIVE)
    assertEquals(review.userId, bookReviewerUserId)

    reviewerBookUserInfo = bookUserInfoDatabase.getUserInfoByUserAndBook(bookReviewerUserId, bookId).get

    review = reviewerBookUserInfo.reviewOption.get
    assertEquals(review.creationTime, new LocalDateTime(2012, 7, 4, 14, 0, 5))
    assertEquals(review.description, "Bardzo dobra ksiazka")
    assertEquals(review.rating, Rating.FIVE)
    assertEquals(review.userId, bookReviewerUserId)


    /*
     * Update book review
     */

    val updateReviewTime = new LocalDateTime(2012, 10, 1, 14, 0, 5)

    bookUserInfoCommand.addOrUpdateReview(bookReviewerUserId, bookId, reviewerBookUserInfo, "Dosc dobra ksiazka", Rating.FOUR, updateReviewTime)


    bookReviews = bookReviewsDatabase.getBookReviews(bookId).get

    assertEquals(bookReviews.userReviews.size, 1)
    review = bookReviews.userReviews.head
    assertEquals(review.creationTime, new LocalDateTime(2012, 10, 1, 14, 0, 5))
    assertEquals(review.description, "Dosc dobra ksiazka")
    assertEquals(review.rating, Rating.FOUR)
    assertEquals(review.userId, bookReviewerUserId)

    reviewerBookUserInfo = bookUserInfoDatabase.getUserInfoByUserAndBook(bookReviewerUserId, bookId).get

    review = reviewerBookUserInfo.reviewOption.get
    assertEquals(review.creationTime, new LocalDateTime(2012, 10, 1, 14, 0, 5))
    assertEquals(review.description, "Dosc dobra ksiazka")
    assertEquals(review.rating, Rating.FOUR)
    assertEquals(review.userId, bookReviewerUserId)


    /*
     * Remove book review
     */

    bookUserInfoCommand.removeBookReview(bookReviewerUserId, reviewerBookUserInfo.id, reviewerBookUserInfo.version)

    reviewerBookUserInfo = bookUserInfoDatabase.getUserInfoByUserAndBook(bookReviewerUserId, bookId).get

    assertTrue(reviewerBookUserInfo.reviewOption.isEmpty)

    bookReviews = bookReviewsDatabase.getBookReviews(bookId).get

    assertTrue(bookReviews.userReviews.isEmpty)


    /*
     * Create book ownership description info By creator
     */

    val bookOwnerUserId = uidGenerator.nextUid

    var ownerBookUserInfo = new BookUserInfo

    val bookOwnership = new BookOwnershipInput

    bookOwnership.owner = true
    bookOwnership.description = "Wydanie polskie"
    bookOwnership.willingToLend = true
    bookOwnership.willingToSell = false
    bookOwnership.wantToBorrow = false
    bookOwnership.wantToBuy = false

    bookUserInfoCommand.addOrUpdateBookOwnership(bookOwnerUserId, bookId, ownerBookUserInfo, bookOwnership)


    ownerBookUserInfo = bookUserInfoDatabase.getUserInfoByUserAndBook(bookOwnerUserId, bookId).get

    val ownership = ownerBookUserInfo.ownershipOption.get
    assertEquals(ownership.owner, true)
    assertEquals(ownership.description, "Wydanie polskie")
    assertEquals(ownership.willingToLend, true)
    assertEquals(ownership.willingToSell, false)
    assertEquals(ownership.wantToBorrow, false)
    assertEquals(ownership.wantToBuy, false)



    val booksOwnedByUser:List[UID] = bookUserInfoDatabase.getBooksOwnedBy(bookOwnerUserId)

    assertEquals(booksOwnedByUser.size, 1)
    assertEquals(booksOwnedByUser.head, bookId)
  }

}
