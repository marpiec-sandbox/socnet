package pl.marpiec.util.validation

import pl.marpiec.util.ValidationResult
import org.apache.commons.lang.StringUtils


/**
 * @author Marcin Pieciukiewicz
 */

object StringValidator {

  val requiredMessage = "That value is required"


  def validate(result: ValidationResult, value: String, minLength: Int, maxLength: Int, errorMessage: String) {
    if (StringUtils.isBlank(value)) {
      result.addError(requiredMessage)
    } else if (isNotValid(value, minLength, maxLength)) {
      result.addError(errorMessage)
    }
  }

  def isNotValid(value: String, minLength: Int, maxLength: Int): Boolean = {
    !isValid(value, minLength, maxLength)
  }

  def isValid(value: String, minLength: Int, maxLength: Int): Boolean = {

    if (minLength < 0 || maxLength < 0 || maxLength < minLength) {
      throw new IllegalArgumentException("Incorrect lenghts, min=" + minLength + ", max=" + maxLength)
    }

    if (value == null) {
      return false
    }
    val trimmed = value.trim
    trimmed.size >= minLength && trimmed.size <= maxLength
  }
}
