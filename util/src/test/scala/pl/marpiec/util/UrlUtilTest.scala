package pl.marpiec.util

/**
 * @author Marcin Pieciukiewicz
 */

import org.testng.annotations.Test
import org.testng.Assert._

@Test
class UrlUtilTest {
  def testAddingProtocol() {
    assertEquals(UrlUtil.addHttpIfNoProtocol("www.socnet.pl"), "http://www.socnet.pl")
    assertEquals(UrlUtil.addHttpIfNoProtocol("ftp://www.socnet.pl"), "ftp://www.socnet.pl")
    assertEquals(UrlUtil.addHttpIfNoProtocol("http://www.socnet.pl"), "http://www.socnet.pl")
    assertEquals(UrlUtil.addHttpIfNoProtocol("https://www.socnet.pl"), "https://www.socnet.pl")
  }

  def testRemovingProtocol() {
    assertEquals(UrlUtil.removeProtocol("www.socnet.pl"), "www.socnet.pl")
    assertEquals(UrlUtil.removeProtocol("ftp://www.socnet.pl"), "www.socnet.pl")
    assertEquals(UrlUtil.removeProtocol("http://www.socnet.pl"), "www.socnet.pl")
    assertEquals(UrlUtil.removeProtocol("https://www.socnet.pl"), "www.socnet.pl")
  }
}
