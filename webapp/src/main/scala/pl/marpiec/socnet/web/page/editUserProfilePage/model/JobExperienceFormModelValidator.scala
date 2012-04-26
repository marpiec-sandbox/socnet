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
    validateDateOrder(result, form)
    
    result
  }

  private def validatePosition(result: ValidationResult, model: JobExperienceFormModel) {
    StringValidator.validate(result, model.position, 3, 100, "Position has to be at least 3 chars, and maximum 100 chars")
  }

  private def validateYearFrom(result: ValidationResult, model: JobExperienceFormModel) {
    if (model.fromYear <= 1900 || model.fromYear >= 2040) {
      result.addError("From year must be between 1900 and 2040")
    }
  }

  private def validateYearTo(result: ValidationResult, model: JobExperienceFormModel) {
    if (!model.currentJob && (model.toYear <= 1900 || model.toYear >= 2040)) {
      result.addError("To year must be between 1900 and 2040")
    }
  }
  private def validateDateOrder(result: ValidationResult, model: JobExperienceFormModel) {
    if(!model.currentJob && model.toYear < model.fromYear) {
      result.addError("The date order is incorrect")
    }
  }
  

}
