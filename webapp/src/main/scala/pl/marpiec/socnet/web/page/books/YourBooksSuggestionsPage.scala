package pl.marpiec.socnet.web.page.books

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.readdatabase.{UserDatabase, BookSuggestionDatabase}
import pl.marpiec.socnet.model.BookSuggestion
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.wicket.SecureAjaxButton
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.component.wicket.form.OneButtonAjaxForm
import pl.marpiec.socnet.service.booksuggestion.BookSuggestionCommand
import org.apache.wicket.markup.html.panel.EmptyPanel

/**
 * @author Marcin Pieciukiewicz
 */

class YourBooksSuggestionsPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookSuggestionDatabase: BookSuggestionDatabase = _
  @SpringBean private var bookSuggestionCommand: BookSuggestionCommand = _

  val suggestions: List[BookSuggestion] = bookSuggestionDatabase.getBooksSuggestionsOfUser(session.userId)

  add(new RepeatingView("booksSuggestions") {

    suggestions.foreach(suggestion => {
      add(new AbstractItem(newChildId()) {

        setOutputMarkupId(true)
        val thisSuggestionComponent = this

        add(new Label("bookTitle", suggestion.title))

        if (suggestion.responseOption.isDefined) {

          val response = suggestion.responseOption.get
          if (response.accepted) {
            add(new Label("status", "Książka została dodana"))
            add(BookPreviewPage.getLink("bookPreviewLink", response.bookIdOption.get))
          } else if (response.declined) {
            add(new Label("status", "Książka nie zostanie dodana"))
            add(new EmptyPanel("bookPreviewLink"))
          } else if (response.alreadyExisted) {
            add(new Label("status", "Książka już istnieje"))
            add(BookPreviewPage.getLink("bookPreviewLink", response.bookIdOption.get))
          }

        } else {
          add(new Label("status", "Czeka na akceptacje"))
          add(new EmptyPanel("bookPreviewLink"))
        }

        add(new OneButtonAjaxForm("removeButton", "Usuń z tej listy", target => {
          bookSuggestionCommand.removeSuggestionFromUserList(session.userId, suggestion.id, suggestion.version)
          thisSuggestionComponent.setVisible(false)
          target.add(thisSuggestionComponent)
        }))

      })
    })
  })

}
