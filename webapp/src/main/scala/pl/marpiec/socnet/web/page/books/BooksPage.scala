package pl.marpiec.socnet.web.page.books

import booksPage.FindBookFormPanel
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.component.book.BookSummaryPreviewPanel
import pl.marpiec.socnet.web.authorization.{AuthorizeUser, AuthorizeTrustedUser, SecureWebPage}
import pl.marpiec.socnet.readdatabase.{BookReviewsDatabase, BookDatabase}
import pl.marpiec.socnet.redundandmodel.book.BookReviews

/**
 * @author Marcin Pieciukiewicz
 */

class BooksPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookDatabase: BookDatabase = _
  @SpringBean private var bookReviewsDatabase: BookReviewsDatabase = _

  val books = bookDatabase.getAllBooks

  add(AuthorizeUser(new BookmarkablePageLink("yourBooksLink", classOf[YourBooksPage])))
  add(AuthorizeTrustedUser(new BookmarkablePageLink("addBookLink", classOf[AddBookPage])))

  add(new FindBookFormPanel("findBook"))


  add(new RepeatingView("book") {

    books.foreach(book => {

      val bookReviews = bookReviewsDatabase.getBookReviews(book.id).getOrElse(new BookReviews)

      add(new AbstractItem(newChildId()) {
        add(new BookSummaryPreviewPanel("bookSummaryPreview", book, bookReviews))
      })
    })
  })
}
