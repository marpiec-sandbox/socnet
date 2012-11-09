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



  add(UnauthorizeAll(new WebMarkupContainer("notLoggedInUserPageContent") {

    add(UnauthorizeAll(new SignInFormPanel("signInPanel")))
    add(UnauthorizeAll(new BookmarkablePageLink("registerLink", classOf[RegisterPage])))

  }))


  add(AuthorizeUser(new WebMarkupContainer("loggedInUserPageContent") {
    add(new ArticlesModule("articlesModule"))
    add(new BooksModule("booksModule"))
    add(new LinksModule("linksModule"))
    add(new MessagesModule("messagesModule"))
    add(new PeopleModule("peopleModule"))
    add(new TechnologiesModule("technologiesModule"))
    add(new SystemMessagesModule("systemMessagesModule"))
  }))

  override protected def isLoginFormVisible = false
}
