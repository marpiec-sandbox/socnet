package pl.marpiec.socnet.web.page.books

import bookPreviewPage.{VoteForBookPanel, BookReviewPreviewPanel, EditReviewFormPanel}
import component.{BooksLinks, BooksLinksPanel, BookOwnershipPanel}
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
import pl.marpiec.socnet.redundandmodel.book.BookReviews
import pl.marpiec.socnet.readdatabase.{BookUserInfoDatabase, BookReviewsDatabase, BookDatabase}
import pl.marpiec.socnet.constant.{SocnetRoles, Rating}
import pl.marpiec.socnet.model.{BookUserInfo, Book}
import pl.marpiec.socnet.web.authorization.{AuthorizeBookEditor, SecureWebPage}
import pl.marpiec.socnet.web.component.simplecomponent.RatingStarsPanel

/**
 * @author Marcin Pieciukiewicz
 */

object BookPreviewPage {
  val BOOK_ID_PARAM = "bookId"

  def getLink(componentId: String, bookId: UID): BookmarkablePageLink[_] = {
    new BookmarkablePageLink(componentId, classOf[BookPreviewPage], getParametersForLink(bookId))
  }

  def getParametersForLink(bookId: UID): PageParameters = {
    new PageParameters().add(BOOK_ID_PARAM, IdProtectionUtil.encrypt(bookId))
  }
}

class BookPreviewPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookDatabase: BookDatabase = _
  @SpringBean private var bookReviewsDatabase: BookReviewsDatabase = _
  @SpringBean private var bookUserInfoDatabase: BookUserInfoDatabase = _

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


  add(new BooksLinksPanel("booksLinksPanel", BooksLinks.BOOK_PREVIEW_LINKS))

  add(AuthorizeBookEditor(AddBookPage.getLinkWithBookId("editBookDesription", bookId)))

  add(new Label("bookTitle", book.description.title))
  add(new Label("polishTitle", book.description.polishTitle))
  add(new Label("authors", book.description.getFormattedAuthorsString))
  add(new Label("isbn", book.description.isbn))
  add(new Label("description", book.description.description))
  var ratingPanel = addAndReturn(new RatingStarsPanel("rating", bookReviews.getAverageRating).setOutputMarkupId(true))
  val votesCountLabel = addAndReturn(new Label("votesCount", bookReviews.getVotesCount.toString).setOutputMarkupId(true))
  var votesLabel = addAndReturn(new Label("votesLabel", labelBasedOnVotesCount(bookReviews.getVotesCount)).setOutputMarkupId(true))

  add(new VoteForBookPanel("voteForBook", this, bookUserInfo, previousUserBookRatingOption))
  

  editReviewLink = addAndReturn(new AjaxLink("showEditReviewFormButton") {
    val thisButton = this
    setOutputMarkupId(true)
    setOutputMarkupPlaceholderTag(true)

    putProperLabelInEditReviewButton(this, currentUserReviewOption)

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
      button.addOrReplace(new Label("label", "Zmień swoją recenzję"))
    } else {
      button.addOrReplace(new Label("label", "Napisz recenzję"))
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

  def labelBasedOnVotesCount(votesCount: Int): String = {
    if (votesCount == 1) {
      "głos"
    } else if (votesCount >= 2 && votesCount <= 4) {
      "głosy"
    } else {
      "głosów"
    }
  }

  def updateBookAvarageRating(target: AjaxRequestTarget, ratingOption: Option[Rating]) {
    ratingPanel = replaceAndReturn(new RatingStarsPanel("rating", bookReviews.getAverageRatingWithOneVoteChanged(session.userId, ratingOption)).setOutputMarkupId(true))
    votesCountLabel.setDefaultModelObject(bookReviews.getVotesCountWithOneVoteChanged(session.userId, ratingOption))
    votesLabel.setDefaultModelObject(labelBasedOnVotesCount(bookReviews.getVotesCount))

    target.add(ratingPanel)
    target.add(votesCountLabel)
    target.add(votesLabel)
  }

}

