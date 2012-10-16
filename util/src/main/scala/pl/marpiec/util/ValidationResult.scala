package pl.marpiec.util

import scala.collection.JavaConversions._
import org.apache.commons.lang.StringUtils

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
  
  def errorsAsFormattedString:String = {
    StringUtils.join(errors.reverse, ", ")
  }
}
