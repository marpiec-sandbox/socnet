package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test

@Test
class IdProtectionUtilTest {

  def testIdCodingAndDecoding() {
    val id = new UID(12345)
    val encrypted:String = IdProtectionUtil.encrypt(id)

    assertEquals(encrypted, "u394ah598pej")

    val decrypted:UID = IdProtectionUtil.decrypt(encrypted)

    assertEquals(id, decrypted)

  }

}
