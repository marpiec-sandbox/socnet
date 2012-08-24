package pl.marpiec.socnet.web.page

import books.BooksPage
import homePage._
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



  setStatelessHint(true)






  add(UnauthorizeAll(new WebMarkupContainer("loginPanel") {
    add(UnauthorizeAll(new SignInFormPanel("signInPanel")))
    add(UnauthorizeAll(new BookmarkablePageLink("registerLink", classOf[RegisterPage])))
  }))

  add(new ArticlesModule("articlesModule"))
  add(new BooksModule("booksModule"))
  add(new LinksModule("linksModule"))
  add(AuthorizeUser(new MessagesModule("messagesModule")))
  add(new PeopleModule("peopleModule"))
  add(AuthorizeUser(new ProfileModule("profileModule")))
  add(new TechnologiesModule("technologiesModule"))

}
