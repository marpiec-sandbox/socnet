package pl.marpiec.util.validation

import pl.marpiec.util.ValidationResult

/**
 * @author Marcin Pieciukiewicz
 */

object StringValidator {

  def validate(result:ValidationResult, value: String, minLength: Int, maxLength: Int, errorMessage:String) {
    if(!validate(value, minLength, maxLength)) {
      result.addError(errorMessage)
    }
  }
  
  def validate(value: String, minLength: Int, maxLength: Int): Boolean = {

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
