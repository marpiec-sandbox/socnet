package pl.marpiec.socnet.web.page.books

import addBookPage.AddBookForm
import component.{BooksLinks, BooksLinksPanel}
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.util.{IdProtectionUtil, UID}
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.BookDatabase

/**
 * @author Marcin Pieciukiewicz
 */

object AddBookPage {
  val BOOK_ID_PARAM = "bookId"

  def getLinkWithBookId(componentId: String, bookId: UID): BookmarkablePageLink[_] = {
    new BookmarkablePageLink(componentId, classOf[AddBookPage], getParametersForLink(bookId))
  }

  def getParametersForLink(bookId: UID): PageParameters = {
    new PageParameters().add(BOOK_ID_PARAM, IdProtectionUtil.encrypt(bookId))
  }
}

class AddBookPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.TRUSTED_USER) {

  @SpringBean private var bookDatabase: BookDatabase = _

  val bookId = IdProtectionUtil.decrypt(parameters.get(BookPreviewPage.BOOK_ID_PARAM).toString)

  val bookOption = bookDatabase.getBookById(bookId)

  add(new BooksLinksPanel("booksLinksPanel", BooksLinks.ADD_BOOK_LINKS))
  add(new AddBookForm("addBookForm", bookOption))
}
