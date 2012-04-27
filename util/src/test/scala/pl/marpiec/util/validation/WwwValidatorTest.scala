package pl.marpiec.util.validation

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.util.ValidationResult

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Test
class WwwValidatorTest {
  def testSimpleValidation() {
    assertTrue(WwwValidator.isNotValid(""))
    assertTrue(WwwValidator.isNotValid(null))
    assertTrue(WwwValidator.isValid("marcin.socnet.pl"))
    assertTrue(WwwValidator.isNotValid("marcin"))
    assertTrue(WwwValidator.isNotValid("marcin.p"))
    assertTrue(WwwValidator.isNotValid("http:marcin.pl"))
    assertTrue(WwwValidator.isNotValid("https//marcin.pl"))
    assertTrue(WwwValidator.isValid("https://marcin.pl"))
    assertTrue(WwwValidator.isValid("http://marcin.pl"))

    val result = new ValidationResult
    WwwValidator.validate(result, "Marcin@socnet@pl")
    assertTrue(result.isNotValid)

  }
}
