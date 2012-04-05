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
    val userDatabase: UserDatabase = new UserDatabaseMockImpl

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
}
