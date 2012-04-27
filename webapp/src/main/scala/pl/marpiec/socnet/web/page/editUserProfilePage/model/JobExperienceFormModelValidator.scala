package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.util.ValidationResult
import pl.marpiec.util.validation.StringValidator

/**
 * @author Marcin Pieciukiewicz
 */

object JobExperienceFormModelValidator {

  def validate(form: JobExperienceFormModel): ValidationResult = {
    val result = new ValidationResult()

    validatePosition(result, form)
    validateYearFrom(result, form)
    validateYearTo(result, form)
    if (result.isValid) {
      validateDateOrder(result, form)
    }

    result
  }

  private def validatePosition(result: ValidationResult, model: JobExperienceFormModel) {
    StringValidator.validate(result, model.position, 3, 100, "Position has to be at least 3 chars, and maximum 100 chars")
  }

  private def validateYearFrom(result: ValidationResult, model: JobExperienceFormModel) {
    try {
      val yearInt = model.fromYear.toInt
      if (yearInt <= 1900 || yearInt >= 2040) {
        result.addError("From year must be between 1900 and 2040")
      }
    } catch {
      case e: NumberFormatException => result.addError("Value is not correct number")
    }
  }

  private def validateYearTo(result: ValidationResult, model: JobExperienceFormModel) {
    try {
      if(!model.currentJob) {
        val yearInt = model.toYear.toInt
        if (yearInt <= 1900 || yearInt >= 2040) {
          result.addError("To year must be between 1900 and 2040")
        }
      }
    } catch {
      case e: NumberFormatException => result.addError("Value is not correct number")
    }
  }

  private def validateDateOrder(result: ValidationResult, model: JobExperienceFormModel) {
    if (!model.currentJob) {
      if (model.toYear < model.fromYear) {
        result.addError("The date order is incorrect")
      } else if (model.toYear == model.fromYear) {
        if (model.toMonth != null && model.fromMonth != null && model.toMonth.order < model.fromMonth.order) {
          result.addError("The date order is incorrect")
        }
      }
    }
  }


}
