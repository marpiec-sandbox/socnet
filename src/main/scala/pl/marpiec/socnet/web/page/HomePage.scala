package pl.marpiec.socnet.web.page

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.authroles.authentication.panel.SignInPanel
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.service.user.{UserQuery, UserCommand}
import pl.marpiec.di.Factory

class HomePage extends WebPage {

  val userCommand:UserCommand = Factory.userCommand

  val userQuery:UserQuery = Factory.userQuery

  add(new Label("welcomeMessage", "Hello World by Wicket version " + getApplication.getFrameworkSettings.getVersion))

  add(new BookmarkablePageLink("signoutLink", classOf[SignOutPage]))
  add(new BookmarkablePageLink("homeLink", classOf[HomePage]))
  add(new BookmarkablePageLink("registerLink", classOf[RegisterPage]))

  add(createSignInPanel);

  add(new Label("userName", getSession.asInstanceOf[SocnetSession].userName))


  def createSignInPanel: SignInPanel = {
    val signInPanel = new SignInPanel("signInPanel", false)
    signInPanel
  }
}
