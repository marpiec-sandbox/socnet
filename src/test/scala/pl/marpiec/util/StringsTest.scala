package pl.marpiec.util

import org.testng.annotations.Test
import org.testng.Assert._

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class StringsTest {

  def testNullSafeStringsComparition() {
    assertTrue(Strings.equal("AAAaaa","AAAaaa"))
    assertTrue(Strings.equal(null,null))
    assertFalse(Strings.equal("AAAaaa","AAAAAA"))
    assertFalse(Strings.equal("AAAaaa",null))
    assertFalse(Strings.equal(null,"AAAAAA"))
  }

}
