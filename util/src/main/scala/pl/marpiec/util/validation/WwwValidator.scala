package pl.marpiec.util.validation

import java.util.regex.Pattern
import pl.marpiec.util.ValidationResult
import org.apache.commons.lang.StringUtils

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

object WwwValidator {

  val requiredMessage = "Web address is required"
  val errorMessage = "Web address is incorrect"

  val pattern = Pattern.compile("^(https:\\/\\/|http:\\/\\/)?[a-z0-9-\\.]+\\.[a-z]{2,4}\\/?([^\\s<>\\#%\"\\,\\{\\}\\\\|\\\\\\^\\[\\]`]+)?$")

  def validate(result: ValidationResult, value: String) {
    if (StringUtils.isBlank(value)) {
      result.addError(requiredMessage)
    } else if (isNotValid(value)) {
      result.addError(errorMessage)
    }
  }

  def isNotValid(value: String): Boolean = {
    !isValid(value)
  }

  def isValid(value: String): Boolean = {
    if (value == null) {
      return false
    }
    val matcher = pattern.matcher(value)
    matcher.matches
  }

}
