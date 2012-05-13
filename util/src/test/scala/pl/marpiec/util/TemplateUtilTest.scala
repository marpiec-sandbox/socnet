package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class TemplateUtilTest {

  val template = "<html><p>Hello #user#.</p><p>Odwiedz nasza nowa strone: #url#</p>" +
    "<p>Nie zapomnij rowniez o #elem1# #elem2# i #elem3#</html>";

  def testSimpleTemplating() {
    var params = Map[String, String]()
    params += "user" -> "Marcin"
    params += "url" -> "www.socnet.pl"
    params += "elem1" -> "element1"
    params += "elem2" -> "element2"
    params += "elem3" -> "element3"

    val mail1 = TemplateUtil.fillTemplate(template, params)
    val mail2 = TemplateUtil.fillTemplate(template, params)


    assertEquals(mail1, "<html><p>Hello Marcin.</p><p>Odwiedz nasza nowa strone: www.socnet.pl</p>" +
      "<p>Nie zapomnij rowniez o element1 element2 i element3</html>")

    assertEquals(mail1, mail2)

  }


}