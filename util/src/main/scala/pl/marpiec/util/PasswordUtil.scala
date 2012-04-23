package pl.marpiec.util

import util.Random
import java.security.MessageDigest
import org.apache.commons.codec.digest.DigestUtils


/**
 * @author Marcin Pieciukiewicz
 */

object PasswordUtil {
  
  val saltLength = 24; //As recommended by OWASP
  val hashComputationTimes = 16000;
  val systemSalt = System.getProperty("SOCNET_SYSTEM_HASH")
  
  def generateRandomSalt:String = Random.nextString(saltLength)
  
  def hashPassword(password:String, salt:String):String = {
    
    if (Strings.isBlank(systemSalt)) {
      throw new IllegalStateException("SOCNET_SYSTEM_HASH is not defined!")
    }

    var digest = systemSalt + password + salt
    for (p <- 0 until hashComputationTimes) {
      digest = systemSalt + DigestUtils.sha512Hex(digest) + salt
    }
    digest
  }
}
