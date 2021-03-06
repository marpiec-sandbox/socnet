package pl.marpiec.util

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang.{StringUtils, RandomStringUtils}


/**
 * @author Marcin Pieciukiewicz
 */

object PasswordUtil {

  val saltLength = 24;
  //As recommended by OWASP
  val hashComputationTimes = 4000;
  val systemSalt = System.getenv("SOCNET_SYSTEM_SALT")

  def generateRandomSalt: String = RandomStringUtils.randomAlphanumeric(saltLength);

  def hashPassword(password: String, salt: String): String = {

    if (StringUtils.isBlank(systemSalt)) {
      throw new IllegalStateException("SOCNET_SYSTEM_SALT is not defined!")
    }

    if (StringUtils.isBlank(password)) {
      throw new IllegalArgumentException("Password musn't be empty")
    }

    if (StringUtils.isBlank(salt)) {
      throw new IllegalArgumentException("Salt musn't be empty")
    }

    var digest = password.trim
    for (p <- 0 until hashComputationTimes) {
      digest = DigestUtils.sha512Hex(systemSalt + digest + salt)
    }
    digest
  }
}
