package pl.marpiec.socnet.model

import org.testng.annotations.Test
import org.testng.Assert._
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserTest {

  def testCopyConstructor {
    val user = new User
    user.uuid = UUID.randomUUID()
    user.version = 1
    user.name = "Marcin Pieciukiewicz"
    user.password = "Haslo"
    user.email = "m.pieciukiewicz@socnet"
    
    val userCopy = user.copy.asInstanceOf[User]

    assertNotSame(userCopy, user)

    assertEquals(userCopy.uuid, user.uuid)
    assertEquals(userCopy.version, user.version)
    assertEquals(userCopy.name, user.name)
    assertEquals(userCopy.password, user.password)
    assertEquals(userCopy.email, user.email)

  }
}
