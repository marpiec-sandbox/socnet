package pl.marpiec.socnet.web.application

import org.testng.annotations.Test
import org.testng.Assert._
import org.apache.wicket.mock.MockWebRequest
import org.apache.wicket.request.Url
import pl.marpiec.di.Factory

@Test
class SocnetSessionTest {

   def testSimpleLogin() {
     val userCommand = Factory.userCommand
     val socnetSession = new SocnetSession(new MockWebRequest(new Url()))

     userCommand.registerUser("Marcin Pieciukiewicz", "m.pieciukiewicz@socnet", "Haslo")

     socnetSession.authenticate("m.pieciukiewicz@socnet", "Haslo")

     assertEquals(socnetSession.userName, "Marcin Pieciukiewicz")

   }
}
