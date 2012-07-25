package pl.marpiec.socnet.service.book

import org.testng.annotations.Test
import org.testng.Assert._

import pl.marpiec.cqrs._
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.book.BookDescription
import pl.marpiec.socnet.model.Book
import pl.marpiec.socnet.readdatabase.{BookDatabaseMockImpl, BookDatabase}

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
    bookDescription.description = "Ksiazka o efektywnym programowaniu w jezyku Java"
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
    assertEquals(book.description.description, "Ksiazka o efektywnym programowaniu w jezyku Java")
    assertEquals(book.description.isbn, "9788324620845")

  }

}
