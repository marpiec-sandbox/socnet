package pl.marpiec.socnet.web.page.registration

import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import pl.marpiec.socnet.readdatabase.exception.EntryAlreadyExistsException
import pl.marpiec.cqrs.exception.IncorrectTriggerException
import org.apache.wicket.markup.html.panel.Fragment
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import pl.marpiec.socnet.service.user.UserCommand
import org.apache.wicket.spring.injection.annot.SpringBean

/**
 * @author Marcin Pieciukiewicz
 */

class TriggerUserRegistrationPage(parameters: PageParameters) extends SimpleTemplatePage {

  @SpringBean
  private var userCommand: UserCommand = _

  val triggerValue = parameters.get(TriggerUserRegistrationPage.TRIGGER_PARAM)
  if (triggerValue.isEmpty) {
    throw new AbortWithHttpErrorCodeException(404);
  }

  try {
    userCommand.triggerUserRegistration(triggerValue.toString)

    add(new Fragment("triggerResult", "registrationSuccessfull", this))

  } catch {
    case ex: EntryAlreadyExistsException => {
      add(new Fragment("triggerResult", "userAlreadyRegistered", this))
    }
    case ex: IncorrectTriggerException => {
      add(new Fragment("triggerResult", "incorrectCode", this))
    }
  }


}

object TriggerUserRegistrationPage {
  val TRIGGER_PARAM = "trigger"
}