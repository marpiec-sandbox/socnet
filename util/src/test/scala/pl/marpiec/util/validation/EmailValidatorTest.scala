package pl.marpiec.util.validation


import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.util.ValidationResult

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class EmailValidatorTest {
  def testSimpleValidation() {
    assertTrue(EmailValidator.isNotValid(""))
    assertTrue(EmailValidator.isNotValid(null))
    assertTrue(EmailValidator.isValid("marcin@socnet.pl"))
    assertTrue(EmailValidator.isValid("marcin@socnet.mobile"))
    assertTrue(EmailValidator.isValid("marcin-p@1.pl"))
    assertTrue(EmailValidator.isValid("marcin.p@1.pl"))

    val result = new ValidationResult
    EmailValidator.validate(result, "Marcin@socnet@pl")
    assertTrue(result.isNotValid)

    assertTrue(EmailValidator.isNotValid("Marcin@socnet@pl"))
    assertTrue(EmailValidator.isNotValid("Marcin@socnet"))
    assertTrue(EmailValidator.isNotValid(".marcin@socnet.pl"))
    assertTrue(EmailValidator.isNotValid("Marcin..p@socnet.pl"))
    assertTrue(EmailValidator.isNotValid("Marcin.p@.socnet.pl"))
    assertTrue(EmailValidator.isNotValid("   Marcin@socnet"))
    assertTrue(EmailValidator.isNotValid("   Marcin@socnet   "))
    assertTrue(EmailValidator.isNotValid("dsfgsdfg"))
    assertTrue(EmailValidator.isNotValid("marcin@%*.pl"))
  }
}
