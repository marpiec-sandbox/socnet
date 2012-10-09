package pl.marpiec.socnet.web.page.books.yourBooksPage

import pl.marpiec.socnet.model.Book
import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.component.book.SimpleBookSummaryPreviewPanel
import pl.marpiec.socnet.web.page.books.component.BookOwnershipPanel

/**
 * @author Marcin Pieciukiewicz
 */

class BookPreviewWithOwnershipPanel(id: String, book: Book) extends Panel(id) {

  add(new SimpleBookSummaryPreviewPanel("simpleBookSummaryPreview", book))
  add(new BookOwnershipPanel("bookOwnershipPanel", book))

}
