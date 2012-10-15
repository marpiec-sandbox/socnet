package pl.marpiec.socnet.service.book

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime
import pl.marpiec.socnet.service.booksuggestion.{BookSuggestionCommandImpl, BookSuggestionCommand}
import pl.marpiec.socnet.readdatabase.BookSuggestionDatabase
import pl.marpiec.socnet.readdatabase.mock.BookSuggestionDatabaseMockImpl

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
    val bookSuggestionId = uidGenerator.nextUid
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


  }
}
