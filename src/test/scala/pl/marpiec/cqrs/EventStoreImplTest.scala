package pl.marpiec.cqrs


import exception.ConcurrentAggregateModificationException
import pl.marpiec.socnet.service.user.{UserCommand, UserCommandImpl}
import pl.marpiec.socnet.model.User
import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.database.{UserDatabaseMockImpl, UserDatabase}

@Test
class EventStoreImplTest {

  def testbasicEventLog () {
    val eventStore:EventStore = new EventStoreImpl
    val entityCache:EntityCache = new EntityCacheSimpleImpl
    val dataStore:DataStore = new DataStoreImpl(eventStore, entityCache)
    val userDatabase:UserDatabase = new UserDatabaseMockImpl

    val userCommand:UserCommand = new UserCommandImpl(eventStore, dataStore, userDatabase)

    val userId = userCommand.registerUser("Marcin", "m.pieciukiewicz@socnet", "Haslo");

    val userAggregateEvents = eventStore.getEvents(classOf[User])

    assertEquals(userAggregateEvents.size, 1)

    var user = dataStore.getEntity(classOf[User], userId).asInstanceOf[User]

    assertEquals(user.name, "Marcin")
    assertEquals(user.email, "m.pieciukiewicz@socnet")

    userCommand.changeUserEmail(userId, user.version, "m.pieciukiewicz@socnet.org")

    user = dataStore.getEntity(classOf[User], userId).asInstanceOf[User]

    assertEquals(user.name, "Marcin")
    assertEquals(user.email, "m.pieciukiewicz@socnet.org")

    try {
      userCommand.changeUserEmail(userId, user.version-1, "irek@socnet")
      fail("ConcurrentAggregateModificationException should be thrown")
    } catch {
      case e:ConcurrentAggregateModificationException => {}
      case e:Exception => {
        fail("Caught illegal exception, ConcurrentAggregateModificationException expected");
      }
    }



  }
}
