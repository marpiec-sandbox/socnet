package pl.marpiec.socnet.web.page.registration

import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.di.Factory
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import pl.marpiec.socnet.database.exception.EntryAlreadyExistsException
import pl.marpiec.cqrs.exception.IncorrectTriggerException
import org.apache.wicket.markup.html.panel.Fragment
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import org.apache.wicket.RestartResponseAtInterceptPageException
import pl.marpiec.socnet.web.page.HomePage

/**
 * @author Marcin Pieciukiewicz
 */

class TriggerUserRegistrationPage(parameters: PageParameters) extends SimpleTemplatePage {
  private val userCommand = Factory.userCommand

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
  val TRIGGER_PARAM = "t"
}