package pl.marpiec.socnet.web.page

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.web.application.SocnetApplication


@Test
class SocnetApplicationTest {

  def testGettingFirstPageOfApplication() {
    val application = new SocnetApplication()

    val homePage = application.getHomePage()
    assertTrue(homePage == classOf[HomePage])

  }
}
