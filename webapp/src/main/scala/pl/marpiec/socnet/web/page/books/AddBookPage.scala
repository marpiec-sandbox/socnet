package pl.marpiec.socnet.web.page.books

import addBookPage.AddBookForm
import component.{BooksLinks, BooksLinksPanel}
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles

/**
 * @author Marcin Pieciukiewicz
 */

class AddBookPage extends SecureWebPage(SocnetRoles.TRUSTED_USER) {

  add(new BooksLinksPanel("booksLinksPanel", BooksLinks.ADD_BOOK_LINKS))
  add(new AddBookForm("addBookForm"))
}
