package pl.marpiec.socnet.service.user.event

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.model.User

@Test
class RegisterUserEventTest {
  def testApplyingEvent() {
    val event = new RegisterUserEvent("Marcin", "Pieciukiewicz", "m.pieciukiewicz@socnet", "Haslo")
    val user = new User

    event.applyEvent(user)

    assertEquals(user.firstName, "Marcin")
    assertEquals(user.lastName, "Pieciukiewicz")
    assertEquals(user.displayName, "Marcin Pieciukiewicz")
    assertEquals(user.email, "m.pieciukiewicz@socnet")
    assertEquals(user.password, "Haslo")
  }
}
