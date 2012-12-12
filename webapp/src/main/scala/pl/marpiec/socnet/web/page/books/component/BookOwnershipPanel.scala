package pl.marpiec.socnet.web.page.books.component

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.model.bookuserinfo.BookOwnership
import pl.marpiec.socnet.web.page.books.bookPreviewPage.model.BookOwnershipFormModel
import pl.marpiec.socnet.service.bookuserinfo.BookUserInfoCommand
import pl.marpiec.socnet.model.BookUserInfo
import pl.marpiec.util.UID
import pl.marpiec.cqrs.AggregatesUtil

/**
 * @author Marcin Pieciukiewicz
 */

class BookOwnershipPanel(id: String, bookId:UID, bookUserInfo: BookUserInfo) extends Panel(id) {

  @SpringBean private var bookUserInfoCommand: BookUserInfoCommand = _

  setOutputMarkupId(true)

  val currentUserId = getSession.asInstanceOf[SocnetSession].userId

  
  val bookOwnership = bookUserInfo.ownershipOption.getOrElse(new BookOwnership)

  add(new StandardAjaxSecureForm[BookOwnershipFormModel]("bookOwnershipForm") {
    def initialize = {
      val model = BookOwnershipFormModel(bookOwnership)
      setModel(new CompoundPropertyModel[BookOwnershipFormModel](model))
      standardCancelButton = false
    }

    def buildSchema = {
      add(new CheckBox("knowThisBook"))
      add(new CheckBox("owner"))
      add(new CheckBox("wantToBuy"))
      add(new CheckBox("willingToSell"))
      add(new CheckBox("wantToBorrow"))
      add(new CheckBox("willingToLend"))
    }

    def onSecureSubmit(target: AjaxRequestTarget, formModel: BookOwnershipFormModel) {

      bookUserInfoCommand.addOrUpdateBookOwnership(currentUserId, bookId, bookUserInfo, formModel.buildBookOwnershipInput())
      AggregatesUtil.incrementVersion(bookUserInfo)

      target.add(BookOwnershipPanel.this)
    }


    def onSecureCancel(target: AjaxRequestTarget, formModel: BookOwnershipFormModel) {
      //ignore, there's no cancel button
    }
  })

}
