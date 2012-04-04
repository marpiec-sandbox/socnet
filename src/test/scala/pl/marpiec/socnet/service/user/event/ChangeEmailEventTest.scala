package pl.marpiec.socnet.service.user.event

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.model.User

@Test
class ChangeEmailEventTest {
  def testApplyingEvent() {
    val event = new ChangeEmailEvent(0, 0, "marcin.p@socnet")
    val user = new User
    user.name = "Marcin"
    user.email = "m.pieciukiewicz@socnet"
    user.password = "Haslo"

    event.applyEvent(user)

    assertEquals(user.name, "Marcin")
    assertEquals(user.email, "marcin.p@socnet")
    assertEquals(user.password, "Haslo")
  }
}
