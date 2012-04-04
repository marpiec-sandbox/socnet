package pl.marpiec.socnet.web.page

import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.panel.FeedbackPanel
import registerPage.RegisterForm


class RegisterPage(parameters: PageParameters) extends WebPage {

  add(new RegisterForm("registerForm"))


}
