package pl.marpiec.socnet.web.page.books

import component.{BooksLinks, BooksLinksPanel}
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.constant.SocnetRoles
import suggestBookPage.SuggestBookForm

/**
 * @author Marcin Pieciukiewicz
 */

class SuggestBookPage extends SecureWebPage(SocnetRoles.TRUSTED_USER) {

  add(new BooksLinksPanel("booksLinksPanel", BooksLinks.ADD_BOOK_SUGGESTION_LINKS))
  add(new SuggestBookForm("suggestBookForm"))
}
