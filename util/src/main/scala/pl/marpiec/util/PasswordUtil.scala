package pl.marpiec.util

import util.Random
import java.security.MessageDigest
import org.apache.commons.codec.digest.DigestUtils


/**
 * @author Marcin Pieciukiewicz
 */

object PasswordUtil {
  
  val saltLength = 24; //As recommended by OWASP
  val hashComputationTimes = 4000;
  val systemSalt = System.getProperty("SOCNET_SYSTEM_SALT")
  
  def generateRandomSalt:String = Random.nextString(saltLength)

  def hashPassword(password:String, salt:String):String = {

    if (Strings.isBlank(systemSalt)) {
      throw new IllegalStateException("SOCNET_SYSTEM_SALT is not defined!")
    }

    if (Strings.isBlank(password)) {
      throw new IllegalArgumentException("Password musn't be empty")
    }

    if (Strings.isBlank(salt)) {
      throw new IllegalArgumentException("Salt musn't be empty")
    }

    var digest = password.trim
    for (p <- 0 until hashComputationTimes) {
      digest = DigestUtils.sha512Hex(systemSalt + digest + salt)
    }
    digest
  }
}
