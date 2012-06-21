package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.util.ValidationResult
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

object AdditionalInfoFormModelValidator {
  def validate(form: AdditionalInfoFormModel): ValidationResult = {
    val result = new ValidationResult()

    validateTitle(result, form)
    validateDescription(result, form)

    result
  }

  private def validateTitle(result: ValidationResult, model: AdditionalInfoFormModel) {
    if (StringUtils.isBlank(model.title)) {
      result.addError("Title is required")
    }
  }

  private def validateDescription(result: ValidationResult, model: AdditionalInfoFormModel) {
    if (StringUtils.isBlank(model.description)) {
      result.addError("Descrition is required")
    }
  }

}
