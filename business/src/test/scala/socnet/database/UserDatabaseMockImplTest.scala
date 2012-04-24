package pl.marpiec.database

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.database.{UserDatabaseMockImpl, UserDatabase}
import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs._
import pl.marpiec.socnet.service.user.event.RegisterUserEvent
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class UserDatabaseMockImplTest {


  def testBasicDatabaseOperations() {
    val userDatabase: UserDatabase = new UserDatabaseMockImpl(new DataStoreImpl(new EventStoreMockImpl, new AggregateCacheSimpleImpl))

    val user: User = new User
    user.firstName = "Marcin"
    user.lastName = "Pieciukiewicz"
    user.passwordHash = "Haslo"
    user.email = "m.pieciukiewicz@socnet"

    userDatabase.addUser(user)
    val userOption = userDatabase.getUserByEmail(user.email)

    assertTrue(userOption.isDefined)
    val userFromDatabase = userOption.get

    assertTrue(user ne userFromDatabase)
    assertEquals(userFromDatabase.firstName, user.firstName)
    assertEquals(userFromDatabase.lastName, user.lastName)
    assertEquals(userFromDatabase.passwordHash, user.passwordHash)
    assertEquals(userFromDatabase.email, user.email)

  }

  def testDataStoreListening() {
    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: AggregateCache = new AggregateCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val userDatabase: UserDatabase = new UserDatabaseMockImpl(dataStore)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val userId = uidGenerator.nextUid
    eventStore.addEventForNewAggregate(userId, new EventRow(new UID(0), userId, 0, new RegisterUserEvent("Marcin", "Pieciukiewicz", "marcin@socnet", "haslo", "salt")))

    val user: Option[User] = userDatabase.getUserById(userId)

    assertTrue(user.isDefined, "Read Database not updated after event occured")

  }


}
