package pl.marpiec.socnet.web.page.books.component

import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.books._
import pl.marpiec.socnet.web.component.book.FindBookFormPanel
import pl.marpiec.socnet.web.authorization.{AuthorizeBookEditor, AuthorizeUser}
import org.apache.wicket.markup.html.panel.{EmptyPanel, Panel}

/**
 * @author Marcin Pieciukiewicz
 */

class BooksLinksPanel(id:String, links:List[Int]) extends Panel(id) {

  add(new FindBookFormPanel("findBookFormPanel"))

  add(AuthorizeUser(new BookmarkablePageLink("booksPageLink", classOf[BooksPage])).setVisible(links.contains(BooksLinks.ALL_BOOKS)))
  add(AuthorizeUser(new BookmarkablePageLink("yourBooksLink", classOf[YourBooksPage])).setVisible(links.contains(BooksLinks.YOUR_BOOKS)))

  add(AuthorizeUser(new BookmarkablePageLink("yourBooksSuggestionsLink", classOf[YourBooksSuggestionsPage])).setVisible(links.contains(BooksLinks.YOUR_SUGGESTIONS)))

  add(AuthorizeUser(new BookmarkablePageLink("suggestBookLink", classOf[SuggestBookPage])).setVisible(links.contains(BooksLinks.ALL_SUGGESTIONS)))


  add(new EmptyPanel("addBookVisibilitySwitch").setVisible(bookEditionSuggestionVisible))
  add(AuthorizeBookEditor(new BookmarkablePageLink("addBookLink", classOf[AddBookPage])).setVisible(links.contains(BooksLinks.ADD_BOOK)))
  add(AuthorizeBookEditor(new BookmarkablePageLink("booksSuggestionsListLink", classOf[BooksSuggestionsListPage])).setVisible(links.contains(BooksLinks.ALL_SUGGESTIONS)))
  
  
  def bookEditionSuggestionVisible:Boolean = {
    links.contains(BooksLinks.ADD_BOOK) ||
    links.contains(BooksLinks.ALL_SUGGESTIONS) ||
    links.contains(BooksLinks.YOUR_SUGGESTIONS)
  }
  
}
