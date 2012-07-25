package pl.marpiec.socnet.web.page.books

import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.authorization.{AuthorizeTrustedUser, SecureWebPage}

/**
 * @author Marcin Pieciukiewicz
 */

class BooksPage extends SecureWebPage(SocnetRoles.USER) {
  add(AuthorizeTrustedUser(new BookmarkablePageLink("addBookLink", classOf[AddBookPage])))
}
