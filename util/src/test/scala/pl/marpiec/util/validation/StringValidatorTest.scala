package pl.marpiec.util.validation

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.util.ValidationResult

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class StringValidatorTest {
  def testSimpleValidation() {

    assertTrue(StringValidator.isValid("abcde", 1, 5))

    assertTrue(StringValidator.isNotValid("abcde", 6, 10))
    assertTrue(StringValidator.isNotValid(null, 1, 10))
    assertTrue(StringValidator.isNotValid("", 1, 10))
    assertTrue(StringValidator.isNotValid("    ", 1, 10))

    val result = new ValidationResult
    StringValidator.validate(result, "    ", 1, 10, "Incorrect value")
    assertTrue(result.isNotValid)

    try {
      StringValidator.isValid("", -1, 10)
      fail()
    } catch {
      case e:IllegalArgumentException => {}
    }

    try {
      StringValidator.isValid("", 10, 4)
      fail()
    } catch {
      case e:IllegalArgumentException => {}
    }

  }

}
