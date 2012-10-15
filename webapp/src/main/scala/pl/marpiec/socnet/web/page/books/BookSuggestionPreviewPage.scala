package pl.marpiec.socnet.web.page.books

import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.{BookSuggestionDatabase, BookDatabase}
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import org.apache.wicket.MarkupContainer._
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.util.{DateUtil, IdProtectionUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

object BookSuggestionPreviewPage {
  val BOOK_SUGGESTION_ID_PARAM = "bookSugegstionId"

  def getLink(componentId:String, bookSuggestionId: UID): BookmarkablePageLink[_] = {
    new BookmarkablePageLink(componentId, classOf[BookSuggestionPreviewPage], getParametersForLink(bookSuggestionId))
  }

  def getParametersForLink(bookSuggestionId: UID): PageParameters = {
    new PageParameters().add(BOOK_SUGGESTION_ID_PARAM, IdProtectionUtil.encrypt(bookSuggestionId))
  }
}

class BookSuggestionPreviewPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookSuggestionDatabase: BookSuggestionDatabase = _

  val bookSuggestionId = IdProtectionUtil.decrypt(parameters.get(BookSuggestionPreviewPage.BOOK_SUGGESTION_ID_PARAM).toString)
  val bookSuggestionOption = bookSuggestionDatabase.getBookSuggestionById(bookSuggestionId)
  val bookSuggestion = bookSuggestionOption.getOrElse(throw new AbortWithHttpErrorCodeException(404))

  add(new Label("creationTime", DateUtil.printDateTime(bookSuggestion.creationTime)))
  add(new Label("bookTitle", bookSuggestion.title))
  add(new Label("polishTitle", bookSuggestion.polishTitle))
  add(new Label("authors", bookSuggestion.getFormattedAuthorsString))
  add(new Label("isbn", bookSuggestion.isbn))
  add(new Label("description", bookSuggestion.description))
  add(new Label("userComment", bookSuggestion.userComment))

}
