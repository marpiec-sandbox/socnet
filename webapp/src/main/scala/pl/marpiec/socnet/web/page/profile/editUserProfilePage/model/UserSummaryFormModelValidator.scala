package pl.marpiec.socnet.web.page.profile.editUserProfilePage.model

import pl.marpiec.util.ValidationResult
import pl.marpiec.util.validation.StringValidator

/**
 * @author Marcin Pieciukiewicz
 */

object UserSummaryFormModelValidator {


  def validate(form: UserSummaryFormModel): ValidationResult = {
    val result = new ValidationResult()

    validateFirstName(result, form)
    validateLastName(result, form)

    result
  }

  private def validateFirstName(result: ValidationResult, model: UserSummaryFormModel) {
    StringValidator.validate(result, model.firstName, 2, 50, "First name has to be at least 2 chars, and maximum 50 chars")
  }

  private def validateLastName(result: ValidationResult, model: UserSummaryFormModel) {
    StringValidator.validate(result, model.lastName, 2, 50, "Last name has to be at least 2 chars, and maximum 50 chars")
  }


}
