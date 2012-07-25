package pl.marpiec.socnet.service.book

import input.BookOwnershipInput
import org.testng.annotations.Test

import pl.marpiec.cqrs._
import pl.marpiec.socnet.readdatabase.{BookDatabaseMockImpl, BookDatabase}
import pl.marpiec.socnet.constant.Rating
import pl.marpiec.socnet.model.book.{BookOwnership, BookDescription}
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

    val bookCommand: BookCommand = new BookCommandImpl(eventStore)

    val bookDescription = new BookDescription
    bookDescription.title = "Effective Java"
    bookDescription.polishTitle = "Java. Efektywne programowanie"
    bookDescription.authors ::= "Joshua Bloch"
    bookDescription.description = "Ksiazka o programowaniu w jezyku Java"
    bookDescription.isbn = "9788324620845"

    val bookCreatorUserId = uidGenerator.nextUid
    val bookId = uidGenerator.nextUid

    bookCommand.createBook(bookCreatorUserId, bookDescription, bookId)

    var bookOption = bookDatabase.getBookById(bookId)

    assertTrue(bookOption.isDefined)

    var book = bookOption.get

    assertEquals(book.description.title, "Effective Java")
    assertEquals(book.description.polishTitle, "Java. Efektywne programowanie")
    assertEquals(book.description.authors.size, 1)
    assertEquals(book.description.authors.head, "Joshua Bloch")
    assertEquals(book.description.description, "Ksiazka o programowaniu w jezyku Java")
    assertEquals(book.description.isbn, "9788324620845")

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

    bookCommand.voteForBook(bookCreatorUserId, book.id, book.version, Rating.FOUR)

    book = bookDatabase.getBookById(bookId).get
    assertEquals(book.reviews.votes(Rating.ONE), 0)
    assertEquals(book.reviews.votes(Rating.TWO), 0)
    assertEquals(book.reviews.votes(Rating.THREE), 0)
    assertEquals(book.reviews.votes(Rating.FOUR), 1)
    assertEquals(book.reviews.votes(Rating.FIVE), 0)

    val bookReviewerUserId = uidGenerator.nextUid

    bookCommand.addOrUpdateReview(bookReviewerUserId, book.id, book.version, "Bardzo dobra ksiazka", Rating.FIVE)

    book = bookDatabase.getBookById(bookId).get

    assertEquals(book.reviews.userReviews.size, 1)
    var review = book.reviews.userReviews.head
    assertEquals(review.description, "Bardzo dobra ksiazka")
    assertEquals(review.rating, Rating.FIVE)
    assertEquals(review.userId, bookReviewerUserId)

    bookCommand.addOrUpdateReview(bookReviewerUserId, book.id, book.version, "Dosc dobra ksiazka", Rating.FOUR)

    book = bookDatabase.getBookById(bookId).get

    assertEquals(book.reviews.userReviews.size, 1)
    review = book.reviews.userReviews.head
    assertEquals(review.description, "Dosc dobra ksiazka")
    assertEquals(review.rating, Rating.FOUR)
    assertEquals(review.userId, bookReviewerUserId)


    val bookOwnerUserId = uidGenerator.nextUid

    val bookOwnership = new BookOwnershipInput

    bookOwnership.owner = true
    bookOwnership.willingToLend = true
    bookOwnership.willingToSell = false
    bookOwnership.wantToBorrow = false
    bookOwnership.wantToBuy = false

    bookCommand.addOrUpdateBookOwnership(bookOwnerUserId, book.id, book.version, bookOwnership)

    book = bookDatabase.getBookById(bookId).get

    assertEquals(book.ownership.size, 1)
    val ownership = book.ownership(bookOwnerUserId)
    assertEquals(ownership.owner, true)
    assertEquals(ownership.willingToLend, true)
    assertEquals(ownership.willingToSell, false)
    assertEquals(ownership.wantToBorrow, false)
    assertEquals(ownership.wantToBuy, false)
  }

}
