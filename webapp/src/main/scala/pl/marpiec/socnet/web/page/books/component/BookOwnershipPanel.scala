package pl.marpiec.socnet.web.page.books.component

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.book.BookCommand
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.model.Book
import pl.marpiec.socnet.model.book.BookOwnership
import pl.marpiec.socnet.web.page.books.bookPreviewPage.model.BookOwnershipFormModel

/**
 * @author Marcin Pieciukiewicz
 */

class BookOwnershipPanel(id: String, book: Book) extends Panel(id) {

  @SpringBean private var bookCommand: BookCommand = _

  setOutputMarkupId(true)

  val currentUserId = getSession.asInstanceOf[SocnetSession].userId

  val bookOwnership = book.ownership.getOrElse(currentUserId, new BookOwnership)

  add(new StandardAjaxSecureForm[BookOwnershipFormModel]("bookOwnershipForm") {
    def initialize = {
      val model = BookOwnershipFormModel(bookOwnership)
      setModel(new CompoundPropertyModel[BookOwnershipFormModel](model))
      standardCancelButton = false
    }

    def buildSchema = {
      add(new CheckBox("owner"))
      add(new CheckBox("wantToBuy"))
      add(new CheckBox("willingToSell"))
      add(new CheckBox("wantToBorrow"))
      add(new CheckBox("willingToLend"))
    }

    def onSecureSubmit(target: AjaxRequestTarget, formModel: BookOwnershipFormModel) {


      bookCommand.addOrUpdateBookOwnership(currentUserId, book.id, book.version, formModel.buildBookOwnershipInput())

      target.add(BookOwnershipPanel.this)
    }


    def onSecureCancel(target: AjaxRequestTarget, formModel: BookOwnershipFormModel) {
      //ignore, there's no cancel button
    }
  })

}
