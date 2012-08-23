package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.books.BooksPage
import pl.marpiec.socnet.web.authorization.AuthorizeUser

/**
 * @author Marcin Pieciukiewicz
 */

class BooksModule(id:String) extends Panel(id) {

  add(AuthorizeUser(new BookmarkablePageLink("booksLink", classOf[BooksPage])))
}
