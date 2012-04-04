package pl.marpiec.database

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.service.user.{UserQueryImpl, UserQuery}
import pl.marpiec.socnet.database.{UserDatabaseMockImpl, UserDatabase}
import pl.marpiec.socnet.model.User

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class SocnetDatabaseMockImplTest {

  def testBasicDatabaseOperations() {
    val socnetDatabase: UserDatabase = new UserDatabaseMockImpl

    val user:User = new User
    user.name = "Marcin Pieciukiewicz"
    user.password = "Haslo"
    user.email = "m.pieciukiewicz@socnet"

    socnetDatabase.addUser(user)
    val userFromDatabase: User = socnetDatabase.getUserByEmail(user.email)

    assertNotNull(userFromDatabase)
    assertTrue(user != userFromDatabase)
    assertEquals(userFromDatabase.name, user.name)
    assertEquals(userFromDatabase.password, user.password)
    assertEquals(userFromDatabase.email, user.email)

  }
}
