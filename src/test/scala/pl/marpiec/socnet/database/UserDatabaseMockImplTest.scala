package pl.marpiec.database

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.service.user.{UserQueryImpl, UserQuery}
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
    val userDatabase: UserDatabase = new UserDatabaseMockImpl(new DataStoreImpl(new EventStoreImpl, new EntityCacheSimpleImpl))

    val user:User = new User
    user.name = "Marcin Pieciukiewicz"
    user.password = "Haslo"
    user.email = "m.pieciukiewicz@socnet"

    userDatabase.addUser(user)
    val userOption = userDatabase.getUserByEmail(user.email)

    assertTrue(userOption.isDefined)
    val userFromDatabase = userOption.get

    assertTrue(user ne userFromDatabase)
    assertEquals(userFromDatabase.name, user.name)
    assertEquals(userFromDatabase.password, user.password)
    assertEquals(userFromDatabase.email, user.email)

  }

  def testDataStoreListening() {
    val eventStore:EventStore = new EventStoreImpl
    val entityCache:EntityCache = new EntityCacheSimpleImpl
    val dataStore:DataStore = new DataStoreImpl(eventStore, entityCache)
    val userDatabase: UserDatabase = new UserDatabaseMockImpl(dataStore)

    val userId = UID.generate
    eventStore.addEventForNewAggregate(userId, new RegisterUserEvent("Marcin", "marcin@socnet", "haslo"))

    val user: Option[User] = userDatabase.getUserById(userId)

    assertTrue(user.isDefined, "Read Database not updated after event occured")

  }


}
