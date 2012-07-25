package pl.marpiec.socnet.web.page

import books.BooksPage
import homePage.ArticleList
import homePage.people.PeopleDashboardPanel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.readdatabase.ArticleDatabase
import pl.marpiec.socnet.web.authorization.{UnauthorizeAll, AuthorizeUser}
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import org.apache.wicket.markup.html.WebMarkupContainer
import registration.RegisterPage
import signin.SignInFormPanel
import org.apache.wicket.spring.injection.annot.SpringBean
import usertechnologies.UserTechnologiesPage

class HomePage extends SimpleTemplatePage {

  @SpringBean private var articleDatabase: ArticleDatabase = _

  setStatelessHint(true)

  add(AuthorizeUser(new BookmarkablePageLink("booksLink", classOf[BooksPage])))
  add(AuthorizeUser(new BookmarkablePageLink("technologiesLink", classOf[UserTechnologiesPage])))
  add(AuthorizeUser(new BookmarkablePageLink("newArticleLink", classOf[NewArticlePage])))

  add(AuthorizeUser(new PeopleDashboardPanel("peopleDashboard")))
  add(AuthorizeUser(new ArticleList("articleList", articleDatabase.getAllArticles)))

  add(UnauthorizeAll(new WebMarkupContainer("loginPanel") {
    add(UnauthorizeAll(new SignInFormPanel("signInPanel")))
    add(UnauthorizeAll(new BookmarkablePageLink("registerLink", classOf[RegisterPage])))
  }))


}
