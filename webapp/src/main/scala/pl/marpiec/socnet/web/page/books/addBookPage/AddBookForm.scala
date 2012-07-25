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

/**
 * @author Marcin Pieciukiewicz
 */

class AddBookForm(id: String) extends Panel(id) {

  @SpringBean private var bookCommand: BookCommand = _
  @SpringBean private var uidGenerator: UidGenerator = _

  add(new SecureForm[AddBookFormModel]("addBookForm") {
    def initialize {
      setModel(new CompoundPropertyModel[AddBookFormModel](new AddBookFormModel))
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

        val newBookId = uidGenerator.nextUid

        val bookDescription = new BookDescription
        bookDescription.title = formModel.title
        bookDescription.polishTitle = formModel.polishTitle
        bookDescription.isbn = formModel.isbn
        bookDescription.description = formModel.description
        bookDescription.authors ::= formModel.authors //TODO change to list

        bookCommand.createBook(getSession.asInstanceOf[SocnetSession].userId, bookDescription, new LocalDateTime, newBookId)


        setResponsePage(classOf[BookPreviewPage], BookPreviewPage.getParametersForLink(newBookId))

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
