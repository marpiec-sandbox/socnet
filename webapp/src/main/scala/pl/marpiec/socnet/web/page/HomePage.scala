package pl.marpiec.socnet.web.page

import homePage._
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.authorization.{UnauthorizeAll, AuthorizeUser}
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import org.apache.wicket.markup.html.WebMarkupContainer
import registration.RegisterPage
import signin.SignInFormPanel

class HomePage extends SimpleTemplatePage {

  setStatelessHint(true)

  add(UnauthorizeAll(new WebMarkupContainer("loginPanel") {
    add(UnauthorizeAll(new SignInFormPanel("signInPanel")))
    add(UnauthorizeAll(new BookmarkablePageLink("registerLink", classOf[RegisterPage])))
  }))

  add(AuthorizeUser(new ArticlesModule("articlesModule")))
  add(AuthorizeUser(new BooksModule("booksModule")))
  add(AuthorizeUser(new LinksModule("linksModule")))
  add(AuthorizeUser(new MessagesModule("messagesModule")))
  add(AuthorizeUser(new PeopleModule("peopleModule")))
  add(AuthorizeUser(new TechnologiesModule("technologiesModule")))

}
