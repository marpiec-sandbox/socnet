package pl.marpiec.socnet.service.user.event

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.model.User

@Test
class ChangeEmailEventTest {
  def testApplyingEvent() {
    val event = new ChangeEmailEvent("marcin.p@socnet")
    val user = new User
    user.firstName = "Marcin"
    user.lastName = "Pieciukiewicz"
    user.email = "m.pieciukiewicz@socnet"
    user.passwordHash = "Haslo"

    event.applyEvent(user)

    assertEquals(user.firstName, "Marcin")
    assertEquals(user.lastName, "Pieciukiewicz")
    assertEquals(user.email, "marcin.p@socnet")
    assertEquals(user.passwordHash, "Haslo")
  }
}
