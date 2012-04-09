package pl.marpiec.socnet.web.page

import newArticle.NewArticlePage
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.authroles.authentication.panel.SignInPanel
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.web.component.articleList.ArticleList
import pl.marpiec.socnet.database.ArticleDatabase
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.web.authorization.{UnauthorizeAll, AuthorizeUser}

class HomePage extends WebPage {

  private val articleDatabase:ArticleDatabase = Factory.articleDatabase
  
  add(new Label("welcomeMessage", "Hello World by Wicket version " + getApplication.getFrameworkSettings.getVersion))

  add(AuthorizeUser(new BookmarkablePageLink("signoutLink", classOf[SignOutPage])))
  add(new BookmarkablePageLink("homeLink", classOf[HomePage]))
  add(UnauthorizeAll(new BookmarkablePageLink("registerLink", classOf[RegisterPage])))
  add(AuthorizeUser(new BookmarkablePageLink("newArticleLink", classOf[NewArticlePage])))

  add(UnauthorizeAll(new SignInPanel("signInPanel", false)))
  
  add(AuthorizeUser(new ArticleList("articleList", articleDatabase.getAllArticles)))


  val session: SocnetSession = getSession.asInstanceOf[SocnetSession]
  if (session.isSignedIn) {
    add(new Label("userName", session.user.name))
  } else {
    add(new Label("userName", ""))
  }



}
