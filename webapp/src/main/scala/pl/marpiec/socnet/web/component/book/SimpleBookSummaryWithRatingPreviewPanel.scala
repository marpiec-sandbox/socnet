package pl.marpiec.socnet.web.component.book

import pl.marpiec.socnet.model.Book
import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.page.books.BookPreviewPage
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.component.simplecomponent.RatingStarsPanel

/**
 * @author Marcin Pieciukiewicz
 */

class SimpleBookSummaryWithRatingPreviewPanel(id: String, book: Book, avarageRating: Double) extends Panel(id) {

  add(BookPreviewPage.getLink("bookPreviewLink", book.id)
    .add(new Label("title", book.description.title))
    .add(new Label("author", book.description.getFormattedAuthorsString))
    .add(new RatingStarsPanel("rating", avarageRating)))

}
