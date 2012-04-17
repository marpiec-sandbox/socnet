package pl.marpiec.util

import org.testng.annotations.Test
import org.testng.Assert._
/**
 * @author Marcin Pieciukiewicz
 */

@Test
class UIDTest {
  
  def testSimpleUIDUsage() {
    val a = new UID(5)
    val b = new UID(10)
    val c = new UID(5)
    
    assertNotEquals(a, b)
    assertNotEquals(b, c)
    assertEquals(a, c)
    assertEquals(a.hashCode(), c.hashCode())

    assertNotEquals(a, 5L)
    
    val e = UID.parseOrZero("35")
    assertEquals(e, new UID(35))

    val f = UID.parseOrZero("s2")
    assertEquals(f, new UID(0))

  }
}
