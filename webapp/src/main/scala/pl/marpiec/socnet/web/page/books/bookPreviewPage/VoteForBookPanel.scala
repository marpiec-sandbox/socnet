package pl.marpiec.socnet.web.page.books.bookPreviewPage

import scala.collection.JavaConversions._
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.bookuserinfo.BookUserInfoCommand
import pl.marpiec.socnet.constant.Rating
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.form.{ChoiceRenderer, DropDownChoice}
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.cqrs.AggregatesUtil
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.model.BookUserInfo
import pl.marpiec.socnet.web.page.books.BookPreviewPage

/**
 * @author Marcin Pieciukiewicz
 */

class VoteForBookPanel(id: String,
                       parentPage: BookPreviewPage,
                       bookUserInfo: BookUserInfo,
                       previousUserBookRatingOption: Option[Rating]) extends Panel(id) {

  @SpringBean private var bookUserInfoCommand: BookUserInfoCommand = _

  val session = getSession.asInstanceOf[SocnetSession]

  add(new DropDownChoice[Rating]("userBookRating",
    new Model[Rating](previousUserBookRatingOption.getOrElse(null)), Rating.values,
    new ChoiceRenderer[Rating]("translation")).add(new AjaxFormComponentUpdatingBehavior("onchange") {
    def onUpdate(target: AjaxRequestTarget) {
      val rating = this.getFormComponent.getModel.getObject.asInstanceOf[Rating]
      val ratingOption = Option(rating)

      if (ratingOption.isDefined) {
        bookUserInfoCommand.voteForBook(session.userId, bookUserInfo.bookId, bookUserInfo, rating)
      } else {
        bookUserInfoCommand.cancelVoteForBook(session.userId, bookUserInfo.bookId, bookUserInfo)
      }

      AggregatesUtil.incrementVersion(bookUserInfo)

      parentPage.updateBookAverageRating(target, ratingOption)
    }
  }))

}
