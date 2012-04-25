package pl.marpiec.socnet.web.page.registerPage

import pl.marpiec.util.ValidationResult
import pl.marpiec.util.validation.{EmailValidator, PasswordValidator, StringValidator}


/**
 * @author Marcin Pieciukiewicz
 */

object RegisterFormValidator {

  def validate(form: RegisterFormModel): ValidationResult = {
    val result = new ValidationResult()

    validateFirstName(result, form)
    validateLastName(result, form)
    validateEmail(result, form)
    validatePassword(result, form)

    result
  }

  private def validateFirstName(result: ValidationResult, model: RegisterFormModel) {
    StringValidator.validate(result, model.firstName, 2, 50, "First name has to be at least 2 chars, and maximum 50 chars")
  }

  private def validateLastName(result: ValidationResult, model: RegisterFormModel) {
    StringValidator.validate(result, model.lastName, 2, 50, "Last name has to be at least 2 chars, and maximum 50 chars")
  }

  private def validateEmail(result: ValidationResult, model: RegisterFormModel) {
    EmailValidator.validate(result, model.email)
  }

  private def validatePassword(result: ValidationResult, model: RegisterFormModel) {
    PasswordValidator.validate(result, model.password, model.repeatPassword)
  }
}
