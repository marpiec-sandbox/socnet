package pl.marpiec.socnet.service.user.event

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.model.User

@Test
class RegisterUserEventTest {
  def testApplyingEvent() {
    val event = new RegisterUserEvent(0, 0, "Marcin", "m.pieciukiewicz@socnet", "Haslo")
    val user = new User

    event.applyEvent(user)

    assertEquals(user.name, "Marcin")
    assertEquals(user.email, "m.pieciukiewicz@socnet")
    assertEquals(user.password, "Haslo")
  }
}
