package pl.marpiec.socnet.web.page.registerPage

import component.RegisterForm
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import org.apache.wicket.request.mapper.parameter.PageParameters

class RegisterPage(parameters: PageParameters) extends SimpleTemplatePage {

  setStatelessHint(true)

  add(new RegisterForm("registerForm"))

}
