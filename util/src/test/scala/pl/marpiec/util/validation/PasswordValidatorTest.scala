package pl.marpiec.util.validation

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.util.ValidationResult

/**
 * @author Marcin Pieciukiewicz
 */

class PasswordValidatorTest {

  def testSimpleValidation() {
    valid("haslo", "haslo")
    invalid("haslo", "BAAAo")
    invalid("haslo", "")
    invalid("abcd", "abcd")
    invalid("abcd  ", "abcd  ")
  }
  
  private def valid(password:String,  repeatPassword:String) {
    var result = new ValidationResult
    PasswordValidator.validate(result, password, repeatPassword)
    assertTrue(result.isValid)
  }

  private def invalid(password:String,  repeatPassword:String) {
    var result = new ValidationResult
    PasswordValidator.validate(result, password, repeatPassword)
    assertFalse(result.isValid)
  }
  
}
