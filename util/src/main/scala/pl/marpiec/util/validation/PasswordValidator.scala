package pl.marpiec.util.validation

import pl.marpiec.util.{ValidationResult, Strings}


/**
 * @author Marcin Pieciukiewicz
 */

object PasswordValidator {

  val requiredMessage = "Password is required"
  val repeatIsRequiredMessage = "Password repeat is required"
  val passwordToShortMessage = "Password is to short, min. 5 chars"
  val passwordToLongMessage = "Password is to long, max. 5 chars"
  val passwordNotTheSameMessage = "Given passwords are not the same"


  def validate(result:ValidationResult, password: String, repeatPassword: String) {
    if (password==null || password.isEmpty) {
      result.addError(requiredMessage)
      return
    }

    if(password.size < 5) {
      result.addError(passwordToShortMessage)
      return
    }

    if(password.size > 64) {
      result.addError(passwordToLongMessage)
      return
    }

    if (repeatPassword==null || repeatPassword.isEmpty) {
      result.addError(repeatIsRequiredMessage)
      return
    }

    if (Strings.notEqual(password, repeatPassword)) {
      result.addError(passwordNotTheSameMessage)
      return
    }
      

  }

}
