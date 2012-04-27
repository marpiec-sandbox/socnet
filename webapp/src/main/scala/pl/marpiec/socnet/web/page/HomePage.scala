package pl.marpiec.socnet.web.page

import homePage.ArticleList
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.authroles.authentication.panel.SignInPanel
import pl.marpiec.socnet.database.ArticleDatabase
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.web.authorization.{UnauthorizeAll, AuthorizeUser}
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage

class HomePage extends SimpleTemplatePage {

  setStatelessHint(true)

  private val articleDatabase: ArticleDatabase = Factory.articleDatabase

  add(UnauthorizeAll(new BookmarkablePageLink("registerLink", classOf[RegisterPage])))
  add(AuthorizeUser(new BookmarkablePageLink("newArticleLink", classOf[NewArticlePage])))
  add(AuthorizeUser(new BookmarkablePageLink("editProfileLink", classOf[EditUserProfilePage])))

  add(UnauthorizeAll(new SignInPanel("signInPanel", false)))

  add(AuthorizeUser(new ArticleList("articleList", articleDatabase.getAllArticles)))


}
