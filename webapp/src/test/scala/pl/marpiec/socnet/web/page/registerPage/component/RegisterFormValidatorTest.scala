package pl.marpiec.socnet.web.page.registerPage.component

import org.testng.annotations.Test
import org.testng.Assert._

/**
 * @author Marcin Pieciukiewicz
 */
@Test
class RegisterFormValidatorTest {

  def createCorrectForm = {
    val form = new RegisterFormModel
    form.email = "marcin@socnet.pl"
    form.username = "Marcin"
    form.password = "haslo"
    form.repeatPassword = "haslo"
    form
  }

  def testCorrectValueValidation() {
    val form = createCorrectForm

    val result = RegisterFormValidator.validate(form)

    assertTrue(result.isValid)

  }

  def testIncorrectNameValidation() {
    val form = createCorrectForm
    form.username = ""

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
