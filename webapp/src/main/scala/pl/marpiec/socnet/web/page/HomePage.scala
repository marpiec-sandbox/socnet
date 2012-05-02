package pl.marpiec.socnet.web.page

import homePage.ArticleList
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.authroles.authentication.panel.SignInPanel
import pl.marpiec.socnet.database.ArticleDatabase
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.web.authorization.{UnauthorizeAll, AuthorizeUser}
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.WebMarkupContainer
import registration.RegisterPage

class HomePage extends SimpleTemplatePage {

  val session = getSession.asInstanceOf[SocnetSession]

  setStatelessHint(true)

  private val articleDatabase: ArticleDatabase = Factory.articleDatabase


  add(AuthorizeUser(new BookmarkablePageLink("newArticleLink", classOf[NewArticlePage])))

  add(AuthorizeUser(new ArticleList("articleList", articleDatabase.getAllArticles)))

  add(UnauthorizeAll(new WebMarkupContainer("loginPanel") {
    add(UnauthorizeAll(new SignInPanel("signInPanel", false)))
    add(UnauthorizeAll(new BookmarkablePageLink("registerLink", classOf[RegisterPage])))
  }))


}
