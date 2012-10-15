package pl.marpiec.socnet.service.book

import org.testng.annotations.Test
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime
import pl.marpiec.socnet.service.booksuggestion.{BookSuggestionCommandImpl, BookSuggestionCommand}
import pl.marpiec.socnet.readdatabase.BookSuggestionDatabase
import pl.marpiec.socnet.readdatabase.mock.BookSuggestionDatabaseMockImpl
import org.testng.Assert._

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class BookSugestionServicesTest {

  def testBookSuggestion() {

    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val bookSuggestionDatabase: BookSuggestionDatabase = new BookSuggestionDatabaseMockImpl(dataStore)
    val bookSuggestionCommand: BookSuggestionCommand = new BookSuggestionCommandImpl(eventStore)

    /**
     * Create book suggestion
     */

    val bookDescription = new BookDescription
    bookDescription.title = "Effective Java"
    bookDescription.polishTitle = "Java. Efektywne programowanie"
    bookDescription.authors ::= "Joshua Bloch"
    bookDescription.description = "Ksiazka o programowaniu w jezyku Java"
    bookDescription.isbn = "9788324620845"

    val userComment = "To bardzo istotna ksi??ka w nauce jezyka."

    val bookSuggestionCreatorUserId = uidGenerator.nextUid
    var bookSuggestionId = uidGenerator.nextUid
    val creationTime = new LocalDateTime(2012, 06, 01, 12, 0, 5)

    bookSuggestionCommand.createBookSuggestion(bookSuggestionCreatorUserId, bookDescription, userComment, creationTime, bookSuggestionId)

    val bookSuggestionOption = bookSuggestionDatabase.getBookSuggestionById(bookSuggestionId)

    assertTrue(bookSuggestionOption.isDefined)

    var bookSuggestion = bookSuggestionOption.get

    assertEquals(bookSuggestion.creationTime, new LocalDateTime(2012, 06, 01, 12, 0, 5))
    assertEquals(bookSuggestion.title, bookDescription.title)
    assertEquals(bookSuggestion.polishTitle, bookDescription.polishTitle)
    assertEquals(bookSuggestion.authors.size, 1)
    assertEquals(bookSuggestion.authors.head, bookDescription.authors.head)
    assertEquals(bookSuggestion.description, bookDescription.description)
    assertEquals(bookSuggestion.isbn, bookDescription.isbn)
    assertEquals(bookSuggestion.userComment, userComment)
    assertEquals(bookSuggestion.responseOption, None)

    /**
     * Accept book suggestion
     */

    val adminUserId = uidGenerator.nextUid
    val createdBookId = uidGenerator.nextUid
    val responseTime = new LocalDateTime(2012, 06, 05, 13, 10, 5)

    bookSuggestionCommand.acceptBookSuggestion(adminUserId, bookSuggestion.id, bookSuggestion.version, createdBookId, responseTime)

    bookSuggestion = bookSuggestionDatabase.getBookSuggestionById(bookSuggestionId).get

    assertTrue(bookSuggestion.responseOption.isDefined)
    var response = bookSuggestion.responseOption.get

    assertEquals(response.time, responseTime)
    assertEquals(response.bookIdOption.get, createdBookId)
    assertEquals(response.accepted, true)
    assertEquals(response.declined, false)
    assertEquals(response.alreadyExisted, false)
    assertTrue(response.commentOption.isEmpty)
    assertFalse(response.userHasSeenResponse)


    /**
     * Create next book suggestion
     */

    bookSuggestionId = uidGenerator.nextUid

    bookSuggestionCommand.createBookSuggestion(bookSuggestionCreatorUserId, bookDescription, userComment, creationTime, bookSuggestionId)

    bookSuggestion = bookSuggestionDatabase.getBookSuggestionById(bookSuggestionId).get
    /**
     * Book already exists response
     */

    bookSuggestionCommand.bookAlreadyExistsForBookSuggestion(adminUserId, bookSuggestion.id, bookSuggestion.version, createdBookId, responseTime)

    bookSuggestion = bookSuggestionDatabase.getBookSuggestionById(bookSuggestionId).get

    assertTrue(bookSuggestion.responseOption.isDefined)
    response = bookSuggestion.responseOption.get

    assertEquals(response.time, responseTime)
    assertEquals(response.bookIdOption.get, createdBookId)
    assertEquals(response.accepted, false)
    assertEquals(response.declined, false)
    assertEquals(response.alreadyExisted, true)
    assertTrue(response.commentOption.isEmpty)
    assertFalse(response.userHasSeenResponse)


    /**
     * Create next book suggestion
     */

    bookSuggestionId = uidGenerator.nextUid

    bookSuggestionCommand.createBookSuggestion(bookSuggestionCreatorUserId, bookDescription, userComment, creationTime, bookSuggestionId)

    bookSuggestion = bookSuggestionDatabase.getBookSuggestionById(bookSuggestionId).get

    /**
     * Book suggestion declined response
     */

    val comment = "Sorry, but won't add this book" 
    bookSuggestionCommand.declineBookSuggestion(adminUserId, bookSuggestion.id, bookSuggestion.version, comment, responseTime)

    bookSuggestion = bookSuggestionDatabase.getBookSuggestionById(bookSuggestionId).get

    assertTrue(bookSuggestion.responseOption.isDefined)
    response = bookSuggestion.responseOption.get

    assertEquals(response.time, responseTime)
    assertTrue(response.bookIdOption.isEmpty)
    assertEquals(response.accepted, false)
    assertEquals(response.declined, true)
    assertEquals(response.alreadyExisted, false)
    assertTrue(response.commentOption.isDefined)
    assertEquals(response.commentOption.get, comment)
    assertFalse(response.userHasSeenResponse)

    /**
     * User reads the response
     */

    bookSuggestionCommand.userHasSeenResponse(bookSuggestionCreatorUserId, bookSuggestion.id, bookSuggestion.version)

    bookSuggestion = bookSuggestionDatabase.getBookSuggestionById(bookSuggestionId).get

    assertTrue(bookSuggestion.responseOption.isDefined)
    response = bookSuggestion.responseOption.get
    assertTrue(response.userHasSeenResponse)


  }
}
