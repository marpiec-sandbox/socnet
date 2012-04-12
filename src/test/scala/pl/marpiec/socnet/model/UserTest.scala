package pl.marpiec.socnet.model

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserTest {

  def testCopyConstructor {
    val user = new User
    user.id = UID.generate
    user.version = 1
    user.name = "Marcin Pieciukiewicz"
    user.password = "Haslo"
    user.email = "m.pieciukiewicz@socnet"
    
    val userCopy = user.copy.asInstanceOf[User]

    assertNotSame(userCopy, user)

    assertEquals(userCopy.id, user.id)
    assertEquals(userCopy.version, user.version)
    assertEquals(userCopy.name, user.name)
    assertEquals(userCopy.password, user.password)
    assertEquals(userCopy.email, user.email)

  }
}
