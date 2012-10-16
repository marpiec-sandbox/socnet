package pl.marpiec.socnet.web.page.books

import bookSuggestionPreviewPage.model.SuggestionFormModel
import component.{BooksLinks, BooksLinksPanel}
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.{BookSuggestionDatabase, BookDatabase}
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.util.{DateUtil, IdProtectionUtil, UID}
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.service.booksuggestion.BookSuggestionCommand
import org.joda.time.LocalDateTime
import org.apache.wicket.markup.html.form.TextArea
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.web.authorization.{AuthorizeUser, SecureWebPage}
import pl.marpiec.socnet.web.component.book.FindBookFormPanel

/**
 * @author Marcin Pieciukiewicz
 */

object BookSuggestionPreviewPage {
  val BOOK_SUGGESTION_ID_PARAM = "bookSugegstionId"

  def getLink(componentId: String, bookSuggestionId: UID): BookmarkablePageLink[_] = {
    new BookmarkablePageLink(componentId, classOf[BookSuggestionPreviewPage], getParametersForLink(bookSuggestionId))
  }

  def getParametersForLink(bookSuggestionId: UID): PageParameters = {
    new PageParameters().add(BOOK_SUGGESTION_ID_PARAM, IdProtectionUtil.encrypt(bookSuggestionId))
  }
}

class BookSuggestionPreviewPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookSuggestionCommand: BookSuggestionCommand = _
  @SpringBean private var bookSuggestionDatabase: BookSuggestionDatabase = _
  @SpringBean private var bookDatabase: BookDatabase = _

  val bookSuggestionId = IdProtectionUtil.decrypt(parameters.get(BookSuggestionPreviewPage.BOOK_SUGGESTION_ID_PARAM).toString)
  val bookSuggestionOption = bookSuggestionDatabase.getBookSuggestionById(bookSuggestionId)
  val bookSuggestion = bookSuggestionOption.getOrElse(throw new AbortWithHttpErrorCodeException(404))

  add(new BooksLinksPanel("booksLinksPanel", BooksLinks.BOOK_SUGGESTION_PREVIEW_LINKS))

  add(new Label("creationTime", DateUtil.printDateTime(bookSuggestion.creationTime)))
  add(new Label("bookTitle", bookSuggestion.title))
  add(new Label("polishTitle", bookSuggestion.polishTitle))
  add(new Label("authors", bookSuggestion.getFormattedAuthorsString))
  add(new Label("isbn", bookSuggestion.isbn))
  add(new Label("description", bookSuggestion.description))
  add(new Label("userComment", bookSuggestion.userComment))

  add(new SecureForm[SuggestionFormModel]("suggestionForm") {

    setVisible(bookSuggestion.responseOption.isEmpty)

    var warningMessageLabel: Label = _
    var bookTitleLabel: Label = _

    def initialize = {
      setModel(new CompoundPropertyModel[SuggestionFormModel](new SuggestionFormModel))
    }

    def buildSchema = {
      warningMessageLabel = new Label("warningMessage")
      warningMessageLabel.setOutputMarkupId(true)
      add(warningMessageLabel)

      bookTitleLabel = new Label("bookTitle")
      bookTitleLabel.setOutputMarkupId(true)
      add(bookTitleLabel)

      add(new TextArea[String]("description"))
      addAcceptButton
      addDeclineButton
      addAlreadyExistsButton
    }

    def addAcceptButton {
      add(new SecureAjaxButton[SuggestionFormModel]("acceptButton") {
        override def onSecureSubmit(target: AjaxRequestTarget, formModel: SuggestionFormModel) {
          val bookId = IdProtectionUtil.decrypt(StringUtils.defaultString(formModel.description).trim)

          val bookOption = bookDatabase.getBookById(bookId)
          if (bookOption.isDefined) {
            if (bookOption.get.description.title == formModel.bookTitle) {
              bookSuggestionCommand.acceptBookSuggestion(session.userId, bookSuggestion.id, bookSuggestion.version, bookId, new LocalDateTime)
              setResponsePage(classOf[BooksSuggestionsListPage])
            } else {
              formModel.bookTitle = bookOption.get.description.title
              formModel.warningMessage = "Tytu? podanej ksi??ki:"
              target.add(warningMessageLabel)
              target.add(bookTitleLabel)
            }
          } else {
            formModel.warningMessage = "Nieprawid?owy identyfikator ksi??ki"
            target.add(warningMessageLabel)
          }

        }
      })
    }

    def addDeclineButton {
      add(new SecureAjaxButton[SuggestionFormModel]("declineButton") {
        override def onSecureSubmit(target: AjaxRequestTarget, formModel: SuggestionFormModel) {
          bookSuggestionCommand.declineBookSuggestion(session.userId, bookSuggestion.id, bookSuggestion.version, formModel.description, new LocalDateTime)
          setResponsePage(classOf[BooksSuggestionsListPage])
        }
      })
    }

    def addAlreadyExistsButton {
      add(new SecureAjaxButton[SuggestionFormModel]("alreadyExistsButton") {
        override def onSecureSubmit(target: AjaxRequestTarget, formModel: SuggestionFormModel) {
          val bookId = IdProtectionUtil.decrypt(StringUtils.defaultString(formModel.description).trim)

          val bookOption = bookDatabase.getBookById(bookId)
          if (bookOption.isDefined) {
            if (bookOption.get.description.title == formModel) {
              bookSuggestionCommand.bookAlreadyExistsForBookSuggestion(session.userId, bookSuggestion.id, bookSuggestion.version, bookId, new LocalDateTime)
              setResponsePage(classOf[BooksSuggestionsListPage])
            } else {
              formModel.bookTitle = bookOption.get.description.title
              formModel.warningMessage = "Tytu? podanej ksi??ki:"
              target.add(warningMessageLabel)
              target.add(bookTitleLabel)
            }
          } else {
            formModel.warningMessage = "Nieprawid?owy identyfikator ksi??ki"
            target.add(warningMessageLabel)
          }
        }
      })
    }

  })
}
