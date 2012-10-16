package pl.marpiec.socnet.web.page.books.suggestBookPage

import model.{SuggestBookFormModelValidator, SuggestBookFormModel}
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.{TextArea, TextField}
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime
import pl.marpiec.socnet.service.booksuggestion.BookSuggestionCommand
import pl.marpiec.socnet.web.page.books.{BookSuggestionPreviewPage, BooksPage}
import org.apache.commons.lang.StringUtils
import org.apache.wicket.Component

/**
 * @author Marcin Pieciukiewicz
 */

class SuggestBookForm(id: String) extends Panel(id) {

  @SpringBean private var bookSuggestionCommand: BookSuggestionCommand = _
  @SpringBean private var uidGenerator: UidGenerator = _

  add(new SecureForm[SuggestBookFormModel]("suggestBookForm") {

    var warningMessageLabel:Component = _

    def initialize {
      setModel(new CompoundPropertyModel[SuggestBookFormModel](new SuggestBookFormModel))
    }

    def buildSchema {
      warningMessageLabel = new Label("warningMessage").setOutputMarkupId(true)
      add(warningMessageLabel)
      add(new TextField[String]("title"))
      add(new TextField[String]("polishTitle"))
      add(new TextField[String]("authors"))
      add(new TextField[String]("isbn"))
      add(new TextArea[String]("description"))
      add(new TextArea[String]("comment"))

      addCancelButton
      addSubmitButton
    }

    def addCancelButton {
      add(new SecureAjaxButton[SuggestBookFormModel]("cancelButton") {
        def onSecureSubmit(target: AjaxRequestTarget, formModel: SuggestBookFormModel) {
          setResponsePage(classOf[BooksPage])
        }
      })
    }

    def addSubmitButton {
      add(new SecureAjaxButton[SuggestBookFormModel]("submitButton") {

        override def onSecureSubmit(target: AjaxRequestTarget, formModel: SuggestBookFormModel) {

          val validationResult = SuggestBookFormModelValidator.validate(formModel)

          if (validationResult.isValid) {

            val newBookSuggestionId = uidGenerator.nextUid

            val bookDescription = new BookDescription
            bookDescription.title = formModel.title
            bookDescription.polishTitle = formModel.polishTitle
            bookDescription.isbn = formModel.isbn
            bookDescription.description = formModel.description

            formModel.authors.split("""\s+,\s+""").foreach(author => {
              if (StringUtils.isNotBlank(author)) {
                bookDescription.authors ::= author
              }
            })

            val currentUserId = getSession.asInstanceOf[SocnetSession].userId
            bookSuggestionCommand.createBookSuggestion(currentUserId, bookDescription, formModel.comment, new LocalDateTime, newBookSuggestionId)

            setResponsePage(classOf[BookSuggestionPreviewPage], BookSuggestionPreviewPage.getParametersForLink(newBookSuggestionId))
          } else {
            formModel.warningMessage = validationResult.errorsAsFormattedString
            target.add(warningMessageLabel)
          }
        }
      })
    }
  })


}
