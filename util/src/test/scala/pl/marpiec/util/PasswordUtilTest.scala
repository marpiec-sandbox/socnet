package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class PasswordUtilTest {


  def testRandomSaltGeneration() {
    val randomSalt = PasswordUtil.generateRandomSalt
    assertEquals(randomSalt.size, 24)
  }

  def testPasswordHashing() {

    val randomSalt = "SOMERONDOMSALT"
    initCryptEngine
    val start = System.currentTimeMillis
    val passwordHash = PasswordUtil.hashPassword("haslo", randomSalt)
    val duration = System.currentTimeMillis - start
    println("Password test finished in " + duration + " ms")
    assertEquals(passwordHash, "3ba4a0e1349755d2da1c01fa7cca8b2fe4cc68efe8c81380fcad00877c9ca573b4c92fe693e9567910deed583c64c68de1e0fcee62e1559689b29d795a914e5d")
    assertTrue(duration < 100, "Hashing didn't finished in 100ms")
  }


  def initCryptEngine {
    PasswordUtil.hashPassword("1", "1") //for crypt engine initialization
  }

  def testIllegalArguments1() {
    try {
      val randomSalt = PasswordUtil.generateRandomSalt
      PasswordUtil.hashPassword("", randomSalt)
      fail("Should throw exception")
    } catch {
      case e: IllegalArgumentException => {}
    }
  }

  def testIllegalArguments2() {
    try {
      PasswordUtil.hashPassword("haslo", "")
      fail("Should throw exception")
    } catch {
      case e: IllegalArgumentException => {}
    }
  }

}
