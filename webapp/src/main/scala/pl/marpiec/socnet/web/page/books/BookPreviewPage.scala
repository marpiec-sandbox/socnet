package pl.marpiec.socnet.web.page.books

import bookPreviewPage.BookOwnershipPanel
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.util.UID
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.{BookDatabase, ArticleDatabase}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.model.{Book, User}

/**
 * @author Marcin Pieciukiewicz
 */

object BookPreviewPage {
  val BOOK_ID_PARAM = "bookId"

  def getLink(book: Book): BookmarkablePageLink[_] = {
    new BookmarkablePageLink("bookPreviewLink", classOf[BookPreviewPage], getParametersForLink(book.id))
  }

  def getParametersForLink(bookId:UID): PageParameters = {
    new PageParameters().add(BOOK_ID_PARAM, bookId)
  }
}

class BookPreviewPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookDatabase: BookDatabase = _

  val bookId = UID.parseOrZero(parameters.get(BookPreviewPage.BOOK_ID_PARAM).toString)

  add(new BookOwnershipPanel("bookOwnership"))

  bookDatabase.getBookById(bookId) match {
    case Some(book) => {

      add(new Label("bookTitle", book.description.title))
      add(new Label("polishTitle", book.description.polishTitle))
      add(new Label("authors", book.description.authors.toString))
      add(new Label("isbn", book.description.isbn))
      add(new Label("description", book.description.description))

    }
    case None => {
      throw new AbortWithHttpErrorCodeException(404);
    }
  }

}

