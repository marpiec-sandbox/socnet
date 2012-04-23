package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.{BeforeTest, Test}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class PasswordUtilTest {

  @BeforeTest
  def init {
    System.setProperty("SOCNET_SYSTEM_SALT", "SOMESYSTEMSALT")
  }

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
    assertEquals(passwordHash, "ca6ceddc6c284089c3d979aff1c0a8349ad245fe31fb3b344010acbe005469770dce132b9673d95137aa20a41246597e5a60f63f42c43f17e0f3e5613d6c7c9d")
    assertTrue(duration < 100)
  }


  def initCryptEngine {
    PasswordUtil.hashPassword("1", "1") //for crypt engine initialization
  }

  @Test(expectedExceptions = Array(classOf[IllegalArgumentException]))
  def testIllegalArguments1() {
    System.setProperty("SOCNET_SYSTEM_SALT", "SOMESYSTEMSALT")
    val randomSalt = PasswordUtil.generateRandomSalt
    PasswordUtil.hashPassword("", randomSalt)
  }

  @Test(expectedExceptions = Array(classOf[IllegalArgumentException]))
  def testIllegalArguments2() {
    System.setProperty("SOCNET_SYSTEM_SALT", "SOMESYSTEMSALT")
    val randomSalt = PasswordUtil.generateRandomSalt
    PasswordUtil.hashPassword("haslo", "")
  }

}
