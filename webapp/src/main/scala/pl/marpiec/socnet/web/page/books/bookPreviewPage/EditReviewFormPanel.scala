package pl.marpiec.socnet.web.page.books.bookPreviewPage

import model.{EditReviewFormModel, EditReviewFormModelValidator}
import scala.collection.JavaConversions._
import org.apache.wicket.markup.html.panel.Panel
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
import pl.marpiec.socnet.web.component.wicket.form.{OneButtonAjaxForm, StandardAjaxSecureForm}
import org.apache.wicket.markup.html.WebMarkupContainer

/**
 * @author Marcin Pieciukiewicz
 */

class EditReviewFormPanel(id: String, book: Book, bookUserInfo: BookUserInfo, parentPage: BookPreviewPage) extends Panel(id) {

  @SpringBean private var bookUserInfoCommand: BookUserInfoCommand = _

  setOutputMarkupId(true)

  val currentUserId = getSession.asInstanceOf[SocnetSession].userId

  add(new OneButtonAjaxForm("removeReviewButton", "Usuń recenzję", target => {

    bookUserInfoCommand.removeBookReview(currentUserId, bookUserInfo.id, bookUserInfo.version)

    bookUserInfo.reviewOption = None
    AggregatesUtil.incrementVersion(bookUserInfo)

    val formPlaceholder = parentPage.addEditReviewFormPlaceholder()
    val editReviewButton = parentPage.showEditReviewButton(None)
    val currentUserReviewPreview = parentPage.addCurrentUserReview(None)

    target.add(formPlaceholder)
    target.add(editReviewButton)
    target.add(currentUserReviewPreview)

  }).setVisible(bookUserInfo.reviewOption.isDefined))

  add(new StandardAjaxSecureForm[EditReviewFormModel]("reviewForm") {

    val thisForm = this

    def initialize = {

      val formModel = new EditReviewFormModel

      val reviewOption = bookUserInfo.reviewOption
      if(reviewOption.isDefined) {
        val review = reviewOption.get
        formModel.rating = review.rating
        formModel.reviewText = review.description
      }

      setModel(new CompoundPropertyModel[EditReviewFormModel](formModel))
    }

    def buildSchema = {

      add(new TextArea[String]("reviewText"))
      add(new DropDownChoice[Rating]("rating", Rating.values, new ChoiceRenderer[Rating]("translation")))

      add(new WebMarkupContainer("removeReviewHolder").setVisible(bookUserInfo.reviewOption.isDefined))
    }

    def onSecureSubmit(target: AjaxRequestTarget, formModel: EditReviewFormModel) {

      if (EditReviewFormModelValidator.validate(formModel)) {

        val creationTime = new LocalDateTime

        bookUserInfoCommand.addOrUpdateReview(currentUserId, book.id, bookUserInfo, formModel.reviewText, formModel.rating, creationTime)
        AggregatesUtil.incrementVersion(bookUserInfo)

        val review = new BookReview
        review.creationTime = creationTime
        review.description = formModel.reviewText
        review.rating = formModel.rating
        review.userId = currentUserId

        bookUserInfo.reviewOption = Option(review)

        val reviewPreviewPanel = parentPage.addCurrentUserReview(bookUserInfo.reviewOption)

        val formPlaceholder = parentPage.addEditReviewFormPlaceholder()
        val editReviewButton = parentPage.showEditReviewButton(bookUserInfo.reviewOption)

        target.add(formPlaceholder)
        target.add(reviewPreviewPanel)
        target.add(editReviewButton)

      } else {
        formModel.warningMessage = "Recenzja musi miec przynajmniej 30 znakow dlugosci oraz musi zostac wybrana ocena ksiazki"
        target.add(this.warningMessageLabel)
      }

    }

    def onSecureCancel(target: AjaxRequestTarget, formModel: EditReviewFormModel) {

      val formPlaceholder = parentPage.addEditReviewFormPlaceholder()
      val editReviewButton = parentPage.showEditReviewButton(bookUserInfo.reviewOption)

      target.add(formPlaceholder)
      target.add(editReviewButton)
    }
  })


}
