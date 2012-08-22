package pl.marpiec.socnet.web.page.books

import scala.collection.JavaConversions._
import bookPreviewPage.{BookReviewPreviewPanel, EditReviewFormPanel, BookOwnershipPanel}
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.util.UID
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.BookDatabase
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.model.Book
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.model.book.BookReview
import org.apache.wicket.markup.html.panel.EmptyPanel
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.markup.html.form.{ChoiceRenderer, DropDownChoice}
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import pl.marpiec.socnet.constant.{Rating, TechnologyCurrentUsage}
import pl.marpiec.socnet.service.book.BookCommand
import org.apache.wicket.model.Model

/**
 * @author Marcin Pieciukiewicz
 */

object BookPreviewPage {
  val BOOK_ID_PARAM = "bookId"

  def getLink(book: Book): BookmarkablePageLink[_] = {
    new BookmarkablePageLink("bookPreviewLink", classOf[BookPreviewPage], getParametersForLink(book.id))
  }

  def getParametersForLink(bookId: UID): PageParameters = {
    new PageParameters().add(BOOK_ID_PARAM, bookId)
  }
}

class BookPreviewPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookDatabase: BookDatabase = _
  @SpringBean private var bookCommand: BookCommand = _

  val thisPage = this

  val bookId = UID.parseOrZero(parameters.get(BookPreviewPage.BOOK_ID_PARAM).toString)

  var editReviewLink: AjaxLink[_] = _
  
  val bookOption = bookDatabase.getBookById(bookId)
  val book = bookOption.getOrElse(throw new AbortWithHttpErrorCodeException(404))

  //init data
  val (notCurrentUserReviews, currentUserReviews) = book.reviews.userReviews.partition(bookReview => {
    bookReview.userId != session.userId
  })
  val currentUserReviewOption = currentUserReviews.headOption
  val previousUserBookRatingOption = book.getUserRating(session.userId)

  //build schema
  add(new BookOwnershipPanel("bookOwnership", book))

  add(new Label("bookTitle", book.description.title))
  add(new Label("polishTitle", book.description.polishTitle))
  add(new Label("authors", book.description.authors.toString))
  add(new Label("isbn", book.description.isbn))
  add(new Label("description", book.description.description))
  val ratingLabel = addAndReturn(new Label("rating", book.getFormattedAverageRating).setOutputMarkupId(true))

  add(new DropDownChoice[Rating]("userBookRating",  
    new Model[Rating](previousUserBookRatingOption.getOrElse(null)), Rating.values,
    new ChoiceRenderer[Rating]("translation")).add(new AjaxFormComponentUpdatingBehavior("onchange") {
    def onUpdate(target: AjaxRequestTarget) {
      val rating = this.getFormComponent.getModel.getObject.asInstanceOf[Rating]

      bookCommand.voteForBook(session.userId, book.id, book.version, rating)

      ratingLabel.setDefaultModelObject(book.getFormattedAverageRatingWithOneVoteChanged(session.userId, rating))
      target.add(ratingLabel)
    }
  }))
  

  editReviewLink = addAndReturn(new AjaxLink("showEditReviewFormButton") {

    setOutputMarkupId(true)
    setOutputMarkupPlaceholderTag(true)
    setVisible(currentUserReviewOption.isEmpty)

    val thisButton = this

    def onClick(target: AjaxRequestTarget) {
      val form = addEditReviewForm(book, None)
      thisButton.setVisible(false)

      target.add(form)
      target.add(thisButton)
    }

  }).asInstanceOf[AjaxLink[_]]

  addEditReviewFormPlaceholder()


  addCurrentUserReview(currentUserReviewOption)

  add(new RepeatingView("bookReviews") {

    notCurrentUserReviews.foreach(bookReview => {
      add(new AbstractItem(newChildId()) {
        add(new BookReviewPreviewPanel("bookReview", bookReview))
      })
    })

  })



  
  
  def addCurrentUserReview(currentUserReviewOption: Option[BookReview]): Component = {

    val CURRENT_USER_REVIEW_PANEL_ID = "currentUserBookReview"

    var panel: Component = null
    if (currentUserReviewOption.isDefined) {
      panel = new BookReviewPreviewPanel(CURRENT_USER_REVIEW_PANEL_ID, currentUserReviewOption.get).setOutputMarkupId(true)
    } else {
      panel = new EmptyPanel(CURRENT_USER_REVIEW_PANEL_ID).setOutputMarkupId(true)
    }

    addOrReplace(panel)
    panel
  }

  def addEditReviewFormPlaceholder(): Component = {
    val component = new EmptyPanel("editReviewForm").setOutputMarkupId(true)
    addOrReplace(component)
    component
  }

  def addEditReviewForm(book: Book, currentUserReviewOption: Option[BookReview]): Component = {
    val component = new EditReviewFormPanel("editReviewForm", book, BookPreviewPage.this)
    addOrReplace(component)
    component
  }

  def showEditReviewButton(): Component = {
    val component = editReviewLink
    editReviewLink.setVisible(true)
    component
  }

}

