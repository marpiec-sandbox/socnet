package pl.marpiec.socnet.service.user

import org.scalatest.FunSuite

import pl.marpiec.socnet.database.{UserDatabaseMockImpl, UserDatabase}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

//@RunWith(classOf[JUnitRunner])
class UserServicesTest extends FunSuite {

  test("Test getting user") {
  /*
    val socnetDatabase: UserDatabase = new UserDatabaseMockImpl
    val userQuery: UserQuery = new UserQueryImpl(socnetDatabase)
    val userCommand: UserCommand = new UserCommandImpl(socnetDatabase)

    assertEquals(null, userQuery.getUserByCredentials("Marcin", "Haslo"))

    val user:User = new User
    user.name = "Marcin Pieciukiewicz"
    user.email = "m.pieciukiewicz@socnet"
    user.password = "Haslo"

    userCommand.registerUser(user)

    assertEquals("Marcin Pieciukiewicz", userQuery.getUserByCredentials("m.pieciukiewicz@socnet", "Haslo"))
    assertNull(userQuery.getUserByCredentials("Marcin", "Nie znam hasla"))   */
  }
}
