package pl.marpiec.socnet.service.user

import org.scalatest.FunSuite

import pl.marpiec.socnet.database.{UserDatabaseMockImpl, UserDatabase}
import pl.marpiec.cqrs._
import exception.ConcurrentAggregateModificationException
import org.testng.Assert._
import org.testng.annotations.Test
import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class UserServicesTest extends FunSuite {

  val eventStore:EventStore = new EventStoreMockImpl
  val entityCache:EntityCache = new EntityCacheSimpleImpl
  val dataStore:DataStore = new DataStoreImpl(eventStore, entityCache)
  val userDatabase:UserDatabase = new UserDatabaseMockImpl(dataStore)
  val uidGenerator:UidGenerator = new UidGeneratorMockImpl

  val userCommand:UserCommand = new UserCommandImpl(eventStore, dataStore, userDatabase, uidGenerator)

  val userId = userCommand.registerUser("Marcin", "m.pieciukiewicz@socnet", "Haslo");

  var user = dataStore.getEntity(classOf[User], userId).asInstanceOf[User]

  assertEquals(user.name, "Marcin")
  assertEquals(user.email, "m.pieciukiewicz@socnet")

  userCommand.changeUserEmail(new UID(0), userId, user.version, "m.pieciukiewicz@socnet.org")

  user = dataStore.getEntity(classOf[User], userId).asInstanceOf[User]

  assertEquals(user.name, "Marcin")
  assertEquals(user.email, "m.pieciukiewicz@socnet.org")

  try {
    userCommand.changeUserEmail(new UID(0), userId, user.version-1, "irek@socnet")
    fail("ConcurrentAggregateModificationException should be thrown")
  } catch {
    case e:ConcurrentAggregateModificationException => {}
    case e:Exception => {
      fail("Caught illegal exception, ConcurrentAggregateModificationException expected");
    }
  }
}
