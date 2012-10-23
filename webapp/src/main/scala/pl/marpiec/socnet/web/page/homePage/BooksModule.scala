package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.authorization.AuthorizeUser
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.{BookReviewsDatabase, BookDatabase}
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.books.{YourBooksPage, BooksPage}
import pl.marpiec.socnet.web.component.book.{FindBookFormPanel, SimpleBookSummaryPreviewPanel}
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class BooksModule(id: String) extends Panel(id) {

  val BEST_BOOKS_COUNT = 5

  @SpringBean private var bookDatabase: BookDatabase = _
  @SpringBean private var bookReviewsDatabase: BookReviewsDatabase = _

  val recentBooks = bookDatabase.getAllBooks
  val bestBooksSimpleInfo = bookReviewsDatabase.getBestBooks(BEST_BOOKS_COUNT)

  val bestBooks = bookDatabase.getBooksByIds(bestBooksSimpleInfo.map(bestBookInfo => bestBookInfo.bookId))

  add(AuthorizeUser(new BookmarkablePageLink("yourBooksLink", classOf[YourBooksPage])))
  add(AuthorizeUser(new BookmarkablePageLink("booksLink", classOf[BooksPage])))

  add(new FindBookFormPanel("findBookFormPanel"))

  add(new RepeatingView("bestBooks") {
    bestBooksSimpleInfo.foreach(bookSimpleInfo => {
      add(new AbstractItem(newChildId()) {
        add(new SimpleBookSummaryPreviewPanel("simpleBookSummaryPreview", bestBooks.find(book => book.id == bookSimpleInfo.bookId).get))
        add(new Label("rating", bookSimpleInfo.averageRating.toString))
      })
    })
  })

  add(new RepeatingView("recentBooks") {
    recentBooks.foreach(book => {

      add(new AbstractItem(newChildId()) {
        add(new SimpleBookSummaryPreviewPanel("simpleBookSummaryPreview", book))
      })
    })
  })

}
