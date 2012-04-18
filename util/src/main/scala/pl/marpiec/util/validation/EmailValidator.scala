package pl.marpiec.util.validation

import java.util.regex.Pattern
import pl.marpiec.util.ValidationResult

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

object EmailValidator {

  val emailPattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")

  def validate(result:ValidationResult, email:String, errorMessage:String) {
    if(isNotValid(email)) {
      result.addError(errorMessage)
    }
  }

  def isNotValid(email:String):Boolean = {
    !isValid(email)
  }

  def isValid(email: String): Boolean = {
    if (email==null) {
      return false
    }
    val matcher = emailPattern.matcher(email)
    matcher.matches
  }

}
