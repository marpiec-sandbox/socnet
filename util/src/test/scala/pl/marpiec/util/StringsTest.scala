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
    assertTrue(Strings.isNotEqual("AAAaaa","AAAAAA"))
    assertTrue(Strings.isNotEqual("AAAaaa",null))
    assertTrue(Strings.isNotEqual(null,"AAAAAA"))
  }

  def testNullBlankTest() {
    assertTrue(Strings.isBlank("    "))
    assertFalse(Strings.isNotBlank("    "))
    assertTrue(Strings.isBlank("\n"))
    assertTrue(Strings.isBlank(null))
    assertFalse(Strings.isBlank("aaa"))
    assertTrue(Strings.isNotBlank("aaa"))

  }
}
