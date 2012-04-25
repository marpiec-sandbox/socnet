package pl.marpiec.socnet.web.page.registerPage.component

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.web.page.registerPage.{RegisterFormValidator, RegisterFormModel}

/**
 * @author Marcin Pieciukiewicz
 */
@Test
class RegisterFormValidatorTest {

  def createCorrectForm = {
    val form = new RegisterFormModel
    form.email = "marcin@socnet.pl"
    form.firstName = "Marcin"
    form.lastName = "Pieciukiewicz"
    form.password = "haslo"
    form.repeatPassword = "haslo"
    form
  }

  def testCorrectValueValidation() {
    val form = createCorrectForm

    val result = RegisterFormValidator.validate(form)

    assertTrue(result.isValid)

  }

  def testIncorrectFirstNameValidation() {
    val form = createCorrectForm
    form.firstName = ""

    val result = RegisterFormValidator.validate(form)

    assertTrue(result.isNotValid)
  }

  def testIncorrectLastNameValidation() {
    val form = createCorrectForm
    form.lastName = ""

    val result = RegisterFormValidator.validate(form)

    assertTrue(result.isNotValid)
  }

  def testToShortPasswordValidation() {
    val form = createCorrectForm
    form.password = "hasl"
    form.repeatPassword = "hasl"

    val result = RegisterFormValidator.validate(form)

    assertTrue(result.isNotValid)
  }

  def testDifferentPasswordsValidation() {
    val form = createCorrectForm
    form.repeatPassword = "hasl"

    val result = RegisterFormValidator.validate(form)

    assertTrue(result.isNotValid)
  }

  def testNoEmailValidation() {
    val form = createCorrectForm
    form.email = ""

    val result = RegisterFormValidator.validate(form)

    assertTrue(result.isNotValid)
  }

  def testIncorrectEmailValidation() {
    val form = createCorrectForm
    form.email = "marcin"

    val result = RegisterFormValidator.validate(form)

    assertTrue(result.isNotValid)
  }
}
