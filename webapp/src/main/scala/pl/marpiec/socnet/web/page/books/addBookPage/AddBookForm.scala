package pl.marpiec.socnet.web.page.books.addBookPage

import model.AddBookFormModel
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.{TextArea, TextField}
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.service.book.BookCommand
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.model.book.BookDescription
import org.joda.time.LocalDateTime
import pl.marpiec.socnet.web.page.books.{BookPreviewPage, BooksPage}
import pl.marpiec.socnet.model.Book
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class AddBookForm(id: String, bookOption: Option[Book]) extends Panel(id) {

  @SpringBean private var bookCommand: BookCommand = _
  @SpringBean private var uidGenerator: UidGenerator = _
  
  val currentUserId = getSession.asInstanceOf[SocnetSession].userId

  add(new SecureForm[AddBookFormModel]("addBookForm") {
    def initialize {
      val model = new AddBookFormModel

      if (bookOption.isDefined) {
        val book = bookOption.get

        model.title = book.description.title
        model.polishTitle = book.description.polishTitle
        model.authors = book.description.getFormattedAuthorsString
        model.isbn = book.description.isbn
        model.description = book.description.description
      }

      setModel(new CompoundPropertyModel[AddBookFormModel](model))
    }

    def buildSchema {
      add(new Label("warningMessage"))
      add(new TextField[String]("title"))
      add(new TextField[String]("polishTitle"))
      add(new TextField[String]("authors"))
      add(new TextField[String]("isbn"))
      add(new TextArea[String]("description"))

      addCancelButton
      addSubmitButton
    }

    def addCancelButton {
      add(new SecureAjaxButton[AddBookFormModel]("cancelButton") {
        def onSecureSubmit(target: AjaxRequestTarget, formModel: AddBookFormModel) {
          setResponsePage(classOf[BooksPage])
        }
      })
    }

    def addSubmitButton {
      add(new SecureAjaxButton[AddBookFormModel]("submitButton") {

        override def onSecureSubmit(target: AjaxRequestTarget, formModel: AddBookFormModel) {

          //TODO add validation



          val bookDescription = new BookDescription
          bookDescription.title = formModel.title
          bookDescription.polishTitle = formModel.polishTitle
          bookDescription.isbn = formModel.isbn
          bookDescription.description = formModel.description
          bookDescription.authors ::= formModel.authors //TODO change to list

          var bookId:UID = null
          if(bookOption.isDefined) {
            val book = bookOption.get
            bookCommand.changeBookDescription(currentUserId, book.id, book.version, bookDescription)
            bookId = book.id
          } else {
            bookId = uidGenerator.nextUid
            bookCommand.createBook(currentUserId, bookDescription, new LocalDateTime, bookId)
          }

          setResponsePage(classOf[BookPreviewPage], BookPreviewPage.getParametersForLink(bookId))




          /*
        val validationResult = UserSummaryFormModelValidator.validate(formModel)
        if (validationResult.isValid) {
          saveChangesToUserProfile(formModel)
          copyFormDataIntoUserAndIncrementVersion(formModel)

          parent.switchToPreviewMode
          formModel.warningMessage = ""
        } else {
          formModel.warningMessage = "Formularz nie zostal wypelniony poprawnie"
        }
        target.add(parent)  */
        }
      })
    }
  })


}
