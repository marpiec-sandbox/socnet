package pl.marpiec.socnet.web.page.forgotPassword

import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import org.apache.wicket.markup.html.form.{PasswordTextField, StatelessForm}
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.HomePage
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.util.validation.PasswordValidator
import pl.marpiec.util.ValidationResult
import org.apache.wicket.markup.html.panel.FeedbackPanel
import pl.marpiec.cqrs.TriggeredEvents
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.service.user.{UserCommand, UserQuery}
import org.apache.wicket.spring.injection.annot.SpringBean

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class TriggerChangeForgottenPasswordPage(parameters: PageParameters) extends SimpleTemplatePage {

  @SpringBean
  private var userCommand: UserCommand = _
  @SpringBean
  private var userQuery: UserQuery = _
  @SpringBean
  private var triggeredEvents: TriggeredEvents = _

  val triggerValue = parameters.get(TriggerChangeForgottenPasswordPage.TRIGGER_PARAM)
  if (triggerValue.isEmpty) {
    throw new AbortWithHttpErrorCodeException(404);
  }

  val trigger = triggerValue.toString

  val resultOption = triggeredEvents.getUserIdAndEventForTrigger(trigger)

  if (resultOption.isEmpty) {
    throw new AbortWithHttpErrorCodeException(404);
  }

  val (userId, event) = resultOption.get

  val user = userQuery.getUserById(userId)

  add(new Label("userName", user.fullName))
  add(new Label("email", user.email))

  add(new FeedbackPanel("feedback"))

  add(new StatelessForm[StatelessForm[_]]("changePasswordForm") {
    var password: String = _
    var repeatPassword: String = _

    setModel(new CompoundPropertyModel[StatelessForm[_]](this.asInstanceOf[StatelessForm[_]]))

    add(new PasswordTextField("password"))
    add(new PasswordTextField("repeatPassword"))

    add(new BookmarkablePageLink("cancelLink", classOf[HomePage]))

    override def onSubmit() {

      val validatorResult = new ValidationResult
      PasswordValidator.validate(validatorResult, password, repeatPassword)

      if (validatorResult.isValid) {
        userCommand.triggerChangePassword(user.id, user.id, user.version, password, trigger)
      } else {
        error(validatorResult.errors.toString())
      }

    }
  })
}

object TriggerChangeForgottenPasswordPage {
  val TRIGGER_PARAM = "t"
}