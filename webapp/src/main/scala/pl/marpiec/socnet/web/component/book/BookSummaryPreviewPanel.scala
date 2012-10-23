package pl.marpiec.socnet.web.component.book

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.Book
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.page.books.BookPreviewPage
import pl.marpiec.socnet.redundandmodel.book.BookReviews
import pl.marpiec.socnet.web.component.simplecomponent.RatingStarsPanel

/**
 * @author Marcin Pieciukiewicz
 */

class BookSummaryPreviewPanel(id: String, book: Book, bookReviews: BookReviews) extends Panel(id) {

  add(BookPreviewPage.getLink("bookPreviewLink", book.id).add(new Label("title", book.description.title)))
  add(new Label("author", book.description.getFormattedAuthorsString))
  add(new RatingStarsPanel("rating", bookReviews.getAverageRating))
  add(new Label("votesCount", bookReviews.getVotesCount.toString))

}
