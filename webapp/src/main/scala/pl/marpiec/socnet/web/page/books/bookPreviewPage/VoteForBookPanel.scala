package pl.marpiec.socnet.web.page.books.bookPreviewPage

import model.VoteForBookFormModel
import scala.collection.JavaConversions._
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.bookuserinfo.BookUserInfoCommand
import pl.marpiec.socnet.constant.Rating
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.cqrs.AggregatesUtil
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.model.BookUserInfo
import pl.marpiec.socnet.web.page.books.BookPreviewPage
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.form.{ChoiceRenderer, DropDownChoice}

/**
 * @author Marcin Pieciukiewicz
 */

class VoteForBookPanel(id: String,
                       parentPage: BookPreviewPage,
                       bookUserInfo: BookUserInfo,
                       previousUserBookRatingOption: Option[Rating]) extends Panel(id) {

  @SpringBean private var bookUserInfoCommand: BookUserInfoCommand = _

  val currentUserId = getSession.asInstanceOf[SocnetSession].userId


  add(new StandardAjaxSecureForm[VoteForBookFormModel]("voteForBookForm") {

    def initialize = {
      standardCancelButton = false
      setModel(new CompoundPropertyModel[VoteForBookFormModel](new VoteForBookFormModel))
    }

    def buildSchema = {
      add(new DropDownChoice[Rating]("userBookRating", Rating.values, new ChoiceRenderer[Rating]("translation")))
    }

    def onSecureSubmit(target: AjaxRequestTarget, formModel: VoteForBookFormModel) {
      val ratingOption = Option(formModel.userBookRating)

      if (ratingOption.isDefined) {
        bookUserInfoCommand.voteForBook(currentUserId, bookUserInfo.bookId, bookUserInfo, formModel.userBookRating)
      } else {
        bookUserInfoCommand.cancelVoteForBook(currentUserId, bookUserInfo.bookId, bookUserInfo)
      }

      AggregatesUtil.incrementVersion(bookUserInfo)

      parentPage.updateBookAverageRating(target, ratingOption)
    }


    def onSecureCancel(target: AjaxRequestTarget, formModel: VoteForBookFormModel) {}
  })


}
