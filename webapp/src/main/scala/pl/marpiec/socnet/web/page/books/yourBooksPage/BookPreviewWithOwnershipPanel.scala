package pl.marpiec.socnet.web.page.books.yourBooksPage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.component.book.SimpleBookSummaryPreviewPanel
import pl.marpiec.socnet.web.page.books.component.BookOwnershipPanel
import pl.marpiec.socnet.model.{BookUserInfo, Book}

/**
 * @author Marcin Pieciukiewicz
 */

class BookPreviewWithOwnershipPanel(id: String, book: Book, bookUserInfo: BookUserInfo) extends Panel(id) {

  add(new SimpleBookSummaryPreviewPanel("simpleBookSummaryPreview", book))
  add(new BookOwnershipPanel("bookOwnershipPanel", book.id, bookUserInfo))

}
