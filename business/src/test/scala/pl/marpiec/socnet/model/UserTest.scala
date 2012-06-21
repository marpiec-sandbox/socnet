package pl.marpiec.socnet.model

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs.{UidGeneratorMockImpl, UidGenerator}

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UserTest {

  def testCopyConstructor() {

    val uidGenerator:UidGenerator = new UidGeneratorMockImpl

    val user = new User
    user.id = uidGenerator.nextUid
    user.version = 1
    user.firstName = "Marcin"
    user.lastName = "Pieciukiewicz"
    user.passwordHash = "Haslo"
    user.email = "m.pieciukiewicz@socnet"
    
    val userCopy = user.copy.asInstanceOf[User]

    assertNotSame(userCopy, user)

    assertEquals(userCopy.id, user.id)
    assertEquals(userCopy.version, user.version)
    assertEquals(userCopy.firstName, user.firstName)
    assertEquals(userCopy.lastName, user.lastName)
    assertEquals(userCopy.passwordHash, user.passwordHash)
    assertEquals(userCopy.email, user.email)

  }
}
