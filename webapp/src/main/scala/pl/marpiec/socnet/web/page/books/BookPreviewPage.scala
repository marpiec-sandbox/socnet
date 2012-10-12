package pl.marpiec.socnet.web.page.books

import component.BookOwnershipPanel
import scala.collection.JavaConversions._
import bookPreviewPage.{BookReviewPreviewPanel, EditReviewFormPanel}
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.util.{IdProtectionUtil, UID}
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.model.bookuserinfo.BookReview
import org.apache.wicket.markup.html.panel.EmptyPanel
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.markup.html.form.{ChoiceRenderer, DropDownChoice}
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import pl.marpiec.socnet.constant.Rating
import org.apache.wicket.model.Model
import pl.marpiec.socnet.redundandmodel.book.BookReviews
import pl.marpiec.socnet.service.bookuserinfo.BookUserInfoCommand
import pl.marpiec.socnet.readdatabase.{BookUserInfoDatabase, BookReviewsDatabase, BookDatabase}
import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.socnet.model.{BookUserInfo, Book}
import pl.marpiec.cqrs.AggregatesUtil
import pl.marpiec.socnet.web.authorization.{AuthorizeUser, SecureWebPage}

/**
 * @author Marcin Pieciukiewicz
 */

object BookPreviewPage {
  val BOOK_ID_PARAM = "bookId"

  def getLink(book: Book): BookmarkablePageLink[_] = {
    new BookmarkablePageLink("bookPreviewLink", classOf[BookPreviewPage], getParametersForLink(book.id))
  }

  def getParametersForLink(bookId: UID): PageParameters = {
    new PageParameters().add(BOOK_ID_PARAM, IdProtectionUtil.encrypt(bookId))
  }
}

class BookPreviewPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookDatabase: BookDatabase = _
  @SpringBean private var bookReviewsDatabase: BookReviewsDatabase = _
  @SpringBean private var bookUserInfoDatabase: BookUserInfoDatabase = _

  @SpringBean private var bookUserInfoCommand: BookUserInfoCommand = _

  val thisPage = this
  val bookId = IdProtectionUtil.decrypt(parameters.get(BookPreviewPage.BOOK_ID_PARAM).toString)
  var editReviewLink: AjaxLink[_] = _
  val bookOption = bookDatabase.getBookById(bookId)
  val book = bookOption.getOrElse(throw new AbortWithHttpErrorCodeException(404))
  val bookReviews = bookReviewsDatabase.getBookReviews(bookId).getOrElse(new BookReviews)
  val bookUserInfo = bookUserInfoDatabase.getUserInfoByUserAndBook(session.userId, bookId).getOrElse(new BookUserInfo)

  //init data
  val (notCurrentUserReviews, currentUserReviews) = bookReviews.userReviews.partition(bookReview => {
    bookReview.userId != session.userId
  })
  val currentUserReviewOption = currentUserReviews.headOption
  val previousUserBookRatingOption = bookReviews.getUserRating(session.userId)

  //build schema
  add(new BookOwnershipPanel("bookOwnership", bookId, bookUserInfo))

  add(AuthorizeUser(new BookmarkablePageLink("yourBooksLink", classOf[YourBooksPage])))
  add(AuthorizeUser(new BookmarkablePageLink("booksLink", classOf[BooksPage])))

  add(new Label("bookTitle", book.description.title))
  add(new Label("polishTitle", book.description.polishTitle))
  add(new Label("authors", book.description.getFormattedAuthorsString))
  add(new Label("isbn", book.description.isbn))
  add(new Label("description", book.description.description))
  val ratingLabel = addAndReturn(new Label("rating", bookReviews.getFormattedAverageRating).setOutputMarkupId(true))
  val votesCountLabel = addAndReturn(new Label("votesCount", bookReviews.getVotesCount.toString).setOutputMarkupId(true))

  add(new DropDownChoice[Rating]("userBookRating",
    new Model[Rating](previousUserBookRatingOption.getOrElse(null)), Rating.values,
    new ChoiceRenderer[Rating]("translation")).add(new AjaxFormComponentUpdatingBehavior("onchange") {
    def onUpdate(target: AjaxRequestTarget) {
      val rating = this.getFormComponent.getModel.getObject.asInstanceOf[Rating]


      bookUserInfoCommand.voteForBook(session.userId, bookId, bookUserInfo, rating)
      AggregatesUtil.incrementVersion(bookUserInfo)

      ratingLabel.setDefaultModelObject(bookReviews.getFormattedAverageRatingWithOneVoteChanged(session.userId, rating))
      votesCountLabel.setDefaultModelObject(bookReviews.getVotesCountWithOneVoteChanged(session.userId))

      target.add(ratingLabel)
      target.add(votesCountLabel)
    }
  }))


  editReviewLink = addAndReturn(new AjaxLink("showEditReviewFormButton") {

    setOutputMarkupId(true)
    setOutputMarkupPlaceholderTag(true)

    putProperLabelInEditReviewButton(this, currentUserReviewOption)

    val thisButton = this

    def onClick(target: AjaxRequestTarget) {
      val form = addEditReviewForm(book)
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
        add(new BookReviewPreviewPanel("bookReview", bookReview, false))
      })
    })

  })

  def putProperLabelInEditReviewButton(button: AjaxLink[_], currentUserReviewOption: Option[BookReview]) {

    if (currentUserReviewOption.isDefined) {
      button.addOrReplace(new Label("label", "Zmień swoją recenzję książki"))
    } else {
      button.addOrReplace(new Label("label", "Napisz recenzję książki"))
    }
  }

  def addCurrentUserReview(currentUserReviewOption: Option[BookReview]): Component = {

    val CURRENT_USER_REVIEW_PANEL_ID = "currentUserBookReview"

    var panel: Component = null
    if (currentUserReviewOption.isDefined) {
      panel = new BookReviewPreviewPanel(CURRENT_USER_REVIEW_PANEL_ID, currentUserReviewOption.get, true).setOutputMarkupId(true)
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

  def addEditReviewForm(book: Book): Component = {
    val component = new EditReviewFormPanel("editReviewForm", book, bookUserInfo, BookPreviewPage.this)
    addOrReplace(component)
    component
  }

  def showEditReviewButton(currentUserReviewOption: Option[BookReview]): Component = {
    putProperLabelInEditReviewButton(editReviewLink, currentUserReviewOption)
    editReviewLink.setVisible(true)
    editReviewLink
  }


}

