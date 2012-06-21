package pl.marpiec.util

/**
 * @author Marcin Pieciukiewicz
 */

class ValidationResult {
  var errors: List[String] = List()

  def addError(error: String) {
    errors = error :: errors
  }

  def isValid = errors.isEmpty

  def isNotValid = errors.nonEmpty
}
