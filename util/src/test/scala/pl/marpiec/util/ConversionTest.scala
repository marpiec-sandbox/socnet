package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ConversionTest {
  def testSimpleConversions() {
    assertEquals(Conversion.emptyIfZero(-5), "-5")
    assertEquals(Conversion.emptyIfZero(0), "")
    assertEquals(Conversion.emptyIfZero(1), "1")
    assertEquals(Conversion.emptyIfZero(5), "5")
  }
}
