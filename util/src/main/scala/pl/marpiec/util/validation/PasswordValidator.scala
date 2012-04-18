package pl.marpiec.util.validation

import pl.marpiec.util.{ValidationResult, Strings}


/**
 * @author Marcin Pieciukiewicz
 */

object PasswordValidator {
  
  def validate(result:ValidationResult, password: String, repeatPassword: String) {
    if (password==null || password.isEmpty) {
      result.addError("Nie podano hasła")
      return
    }

    if (repeatPassword==null || repeatPassword.isEmpty) {
      result.addError("Nie potwierdzono hasła")
      return
    }

    if (Strings.notEqual(password, repeatPassword)) {
      result.addError("Podane hasła nie są równe")
      return
    }
      
    if(password.size < 5) {
      result.addError("Hasło musi mieć przynajmniej 5 znaków")
      return
    }

    if(password.size > 64) {
      result.addError("Hasło musi mieć maksymalnie 64 znaki")
      return
    }
  }

}
