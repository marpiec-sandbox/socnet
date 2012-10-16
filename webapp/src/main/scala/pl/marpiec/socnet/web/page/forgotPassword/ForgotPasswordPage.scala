package pl.marpiec.socnet.web.page.forgotPassword

import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import org.apache.wicket.markup.html.form.TextField
import scala.Predef._
import pl.marpiec.util.validation.EmailValidator
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.HomePage
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.util.ValidationResult
import pl.marpiec.socnet.service.user.UserCommand
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.web.wicket.SimpleStatelessForm

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class ForgotPasswordPage extends SimpleTemplatePage {

  @SpringBean private var userCommand: UserCommand = _

  add(new SimpleStatelessForm("form") {

    var email: String = _
    var warningMessage: String = ""

    add(new Label("warningMessage"))
    add(new TextField[String]("email"))
    add(new BookmarkablePageLink("cancelLink", classOf[HomePage]))


    override def onSubmit() {

      val validationResult = new ValidationResult

      EmailValidator.validate(validationResult, email)

      if (validationResult.isValid) {

        userCommand.createChangeForgottenPasswordTrigger(email)
        setResponsePage(classOf[ConfirmForgotPasswordPage])

      } else {
        warningMessage = validationResult.errorsAsFormattedString
      }

    }
  })

}
