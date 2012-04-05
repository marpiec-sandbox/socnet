package pl.marpiec.socnet.web.page

import newArticle.NewArticlePage
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.authroles.authentication.panel.SignInPanel
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.web.authorization.AuthorizeUser

class HomePage extends WebPage {

  add(new Label("welcomeMessage", "Hello World by Wicket version " + getApplication.getFrameworkSettings.getVersion))

  add(new BookmarkablePageLink("signoutLink", classOf[SignOutPage]))
  add(new BookmarkablePageLink("homeLink", classOf[HomePage]))
  add(new BookmarkablePageLink("registerLink", classOf[RegisterPage]))
  add(AuthorizeUser(new BookmarkablePageLink("newArticleLink", classOf[NewArticlePage])))

  add(createSignInPanel)


  val session: SocnetSession = getSession.asInstanceOf[SocnetSession]
  if (session.isSignedIn) {
    add(new Label("userName", session.user.name))
  } else {
    add(new Label("userName", ""))
  }



  def createSignInPanel: SignInPanel = {
    val signInPanel = new SignInPanel("signInPanel", false)
    signInPanel
  }
}
