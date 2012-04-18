package pl.marpiec.socnet.web.page.registerPage.component

import pl.marpiec.util.ValidationResult
import pl.marpiec.util.validation.{PasswordValidator, StringValidator}


/**
 * @author Marcin Pieciukiewicz
 */

object RegisterFormValidator {

  def validate(form:RegisterFormModel):ValidationResult = {
    val result = new ValidationResult()

      validateUserName(result, form)
      validateEmail(result, form)
      validatePassword(result, form)

    result
  }

  def validateUserName(result:ValidationResult, model: RegisterFormModel) {
    StringValidator.validate(result, model.username, 5, 50, "Username has to be at least 5 chars, and maximum 50 chars")
  }

  def validateEmail(result:ValidationResult, model: RegisterFormModel) {

  }

  def validatePassword(result: ValidationResult, model: RegisterFormModel) {
    PasswordValidator.validate(result, model.password, model.repeatPassword)
  }
}
