package pl.marpiec.socnet.web.component.book

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.Book
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.page.books.BookPreviewPage
import pl.marpiec.socnet.redundandmodel.book.BookReviews

/**
 * @author Marcin Pieciukiewicz
 */

class BookSummaryPreviewPanel(id: String, book: Book, bookReviews: BookReviews) extends Panel(id) {

  add(BookPreviewPage.getLink(book).add(new Label("title", book.description.title)))
  add(new Label("author", book.description.getFormattedAuthorsString))
  add(new Label("rating", bookReviews.getFormattedAverageRating))
  add(new Label("votesCount", bookReviews.getVotesCount.toString))

}
