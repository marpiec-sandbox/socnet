package pl.marpiec.socnet.web.page.registerPage

import component.RegisterForm
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.WebPage


class RegisterPage(parameters: PageParameters) extends WebPage {

  setStatelessHint(true)

  add(new RegisterForm("registerForm"))

}
