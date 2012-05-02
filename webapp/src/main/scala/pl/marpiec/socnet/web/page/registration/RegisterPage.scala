package pl.marpiec.socnet.web.page.registration

import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import org.apache.wicket.request.mapper.parameter.PageParameters
import registerPage.RegisterForm

class RegisterPage(parameters: PageParameters) extends SimpleTemplatePage {

  setStatelessHint(true)

  add(new RegisterForm("registerForm"))

}
