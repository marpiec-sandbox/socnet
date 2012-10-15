package pl.marpiec.socnet.web.page.books

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.repeater.RepeatingView
import pl.marpiec.socnet.redundandmodel.book.BookReviews
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.repeater.RepeatingView._
import pl.marpiec.socnet.web.component.book.BookSummaryPreviewPanel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.readdatabase.{UserDatabase, BookSuggestionDatabase}
import pl.marpiec.socnet.model.{User, BookSuggestion}
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage

/**
 * @author Marcin Pieciukiewicz
 */

class BooksSuggestionsListPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookSuggestionDatabase: BookSuggestionDatabase = _
  @SpringBean private var userDatabase: UserDatabase = _

  val suggestions:List[BookSuggestion] = bookSuggestionDatabase.getAllUnrespondedSuggestions

  val users = userDatabase.getUsersByIds(suggestions.map(suggestion => suggestion.userId))

  add(new RepeatingView("booksSuggestions") {

    suggestions.foreach(suggestion => {
      add(new AbstractItem(newChildId()) {

        val user = users.find(user => user.id == suggestion.userId).get

        add(BookSuggestionPreviewPage.getLink("bookSuggestionPreviewLink", suggestion.id).add(new Label("bookTitle", suggestion.title)))
        add(UserProfilePreviewPage.getLink(user).add(new Label("userName", user.fullName)))

      })
    })
  })
  
}
