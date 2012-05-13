package pl.marpiec.socnet.web.page

import homePage.ArticleList
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.readdatabase.ArticleDatabase
import pl.marpiec.socnet.web.authorization.{UnauthorizeAll, AuthorizeUser}
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.WebMarkupContainer
import registration.RegisterPage
import signin.SignInFormPanel
import org.apache.wicket.spring.injection.annot.SpringBean

class HomePage extends SimpleTemplatePage {

  val session = getSession.asInstanceOf[SocnetSession]

  setStatelessHint(true)

  @SpringBean
  private var articleDatabase: ArticleDatabase = _


  add(AuthorizeUser(new BookmarkablePageLink("newArticleLink", classOf[NewArticlePage])))

  add(AuthorizeUser(new ArticleList("articleList", articleDatabase.getAllArticles)))

  add(UnauthorizeAll(new WebMarkupContainer("loginPanel") {
    add(UnauthorizeAll(new SignInFormPanel("signInPanel")))
    add(UnauthorizeAll(new BookmarkablePageLink("registerLink", classOf[RegisterPage])))
  }))


}
