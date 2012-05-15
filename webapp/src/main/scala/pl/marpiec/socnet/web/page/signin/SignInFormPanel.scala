package pl.marpiec.socnet.web.page.signin

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.panel.{FeedbackPanel, Panel}
import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.forgotPassword.ForgotPasswordPage
import pl.marpiec.socnet.web.wicket.SimpleStatelessForm

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SignInFormPanel(id: String) extends Panel(id) {

  add(new FeedbackPanel("feedback"))

  add(new SimpleStatelessForm("signInForm") {

    var username: String = _
    var password: String = _
    var rememberMe: Boolean = _


    add(new TextField[String]("username"))
    add(new PasswordTextField("password"))
    add(new CheckBox("rememberMe"))
    add(new BookmarkablePageLink("forgotPasswordLink", classOf[ForgotPasswordPage]))

    override def onSubmit() {

      val authenticationStrategy = getApplication.getSecuritySettings.getAuthenticationStrategy

      val signInResult = AuthenticatedWebSession.get.signIn(username, password)

      if (signInResult) {
        if (rememberMe) {
          authenticationStrategy.save(username, password);
        } else {
          authenticationStrategy.remove();
        }
        //tu można zrobić continueTOOriginalPage, ale niekoniecznie to dobrze działą
        setResponsePage(getApplication.getHomePage)
      } else {
        error(getLocalizer.getString("signInFailed", this, "Sign in failed"))
        authenticationStrategy.remove();
      }

    }
  })

}
