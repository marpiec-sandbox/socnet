package pl.marpiec.socnet.web.page.books.bookPreviewPage

import pl.marpiec.socnet.model.bookuserinfo.BookReview
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.UserDatabase
import org.apache.wicket.markup.html.panel.{EmptyPanel, Panel}
import org.apache.wicket.markup.html.WebMarkupContainer

/**
 * @author Marcin Pieciukiewicz
 */

class BookReviewPreviewPanel(id: String, review: BookReview, authorIsCurrentUser: Boolean) extends Panel(id) {

  @SpringBean private var userDatabase: UserDatabase = _

  val userOption = userDatabase.getUserById(review.userId)

  add(new Label("reviewText", review.description))
  add(new Label("reviewRating", review.rating.numericValue.toString))

  if (authorIsCurrentUser) {
    add(new WebMarkupContainer("profileLink").add(new EmptyPanel("userName")))
    add(new WebMarkupContainer("yourReview"))
  } else {

    if (userOption.isDefined) {
      val user = userOption.get
      add(UserProfilePreviewPage.getLink(user).add(new Label("userName", user.fullName)))
    } else {
      add(new WebMarkupContainer("profileLink").add(new EmptyPanel("userName")))
    }

    add(new WebMarkupContainer("yourReview").setVisible(false))

  }


}
