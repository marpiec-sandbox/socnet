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

    assertTrue(StringValidator.validate("abcde", 1, 5))

    assertFalse(StringValidator.validate("abcde", 6, 10))
    assertFalse(StringValidator.validate("", 1, 10))
    assertFalse(StringValidator.validate("    ", 1, 10))

    val result = new ValidationResult
    StringValidator.validate(result, "    ", 1, 10, "Incorrect value")
    assertTrue(result.isNotValid)

    try {
      StringValidator.validate("", -1, 10)
      fail()
    } catch {
      case e:IllegalArgumentException => {}
    }

    try {
      StringValidator.validate("", 10, 4)
      fail()
    } catch {
      case e:IllegalArgumentException => {}
    }

  }

}
