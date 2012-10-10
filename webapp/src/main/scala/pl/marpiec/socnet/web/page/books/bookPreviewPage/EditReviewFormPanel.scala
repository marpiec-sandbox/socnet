package pl.marpiec.socnet.web.page.books.bookPreviewPage

import scala.collection.JavaConversions._
import model.{EditReviewFormModelValidator, EditReviewFormModel}
import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.web.application.SocnetSession
import org.joda.time.LocalDateTime
import org.apache.wicket.markup.html.form.{ChoiceRenderer, DropDownChoice, TextArea}
import pl.marpiec.socnet.constant.Rating
import pl.marpiec.socnet.web.page.books.BookPreviewPage
import pl.marpiec.socnet.model.bookuserinfo.BookReview
import pl.marpiec.socnet.service.bookuserinfo.BookUserInfoCommand
import pl.marpiec.socnet.model.{BookUserInfo, Book}
import pl.marpiec.cqrs.AggregatesUtil

/**
 * @author Marcin Pieciukiewicz
 */

class EditReviewFormPanel(id: String, book: Book, bookUserInfo: BookUserInfo, parentPage: BookPreviewPage) extends Panel(id) {

  @SpringBean private var bookUserInfoCommand: BookUserInfoCommand = _

  setOutputMarkupId(true)

  add(new StandardAjaxSecureForm[EditReviewFormModel]("reviewForm") {

    val thisForm = this

    def initialize = {
      setModel(new CompoundPropertyModel[EditReviewFormModel](new EditReviewFormModel))
    }

    def buildSchema = {

      add(new TextArea[String]("reviewText"))
      add(new DropDownChoice[Rating]("rating", Rating.values, new ChoiceRenderer[Rating]("translation")))

    }

    def onSecureSubmit(target: AjaxRequestTarget, formModel: EditReviewFormModel) {

      if (EditReviewFormModelValidator.validate(formModel)) {

        val creationTime = new LocalDateTime
        val currentUserId = getSession.asInstanceOf[SocnetSession].userId

        
        bookUserInfoCommand.addOrUpdateReview(currentUserId, book.id, bookUserInfo, formModel.reviewText, formModel.rating, creationTime)
        AggregatesUtil.incrementVersion(bookUserInfo)

        val review = new BookReview
        review.creationTime = creationTime
        review.description = formModel.reviewText
        review.rating = formModel.rating
        review.userId = currentUserId

        val reviewPreviewPanel = parentPage.addCurrentUserReview(Option(review))

        val formPlaceholder = parentPage.addEditReviewFormPlaceholder()

        target.add(formPlaceholder)
        target.add(reviewPreviewPanel)

      } else {
        formModel.warningMessage = "Recenzja musi miec przynajmniej 30 znakow dlugosci oraz musi zostac wybrana ocena ksiazki"
        target.add(this.warningMessageLabel)
      }

    }

    def onSecureCancel(target: AjaxRequestTarget, formModel: EditReviewFormModel) {

      val formPlaceholder = parentPage.addEditReviewFormPlaceholder()
      val editReviewButton = parentPage.showEditReviewButton()

      target.add(formPlaceholder)
      target.add(editReviewButton)
    }
  })


}
