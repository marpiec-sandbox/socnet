package pl.marpiec.socnet.web.page.books.bookPreviewPage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.book.BookReview
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class BookReviewPreviewPanel(id: String, review: BookReview) extends Panel(id) {

  add(new Label("reviewText", review.description))

}
