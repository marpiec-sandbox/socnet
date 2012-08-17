package pl.marpiec.socnet.web.page.books

import booksPage.FindBookFormPanel
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.authorization.{AuthorizeTrustedUser, SecureWebPage}
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.BookDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.component.book.BookSummaryPreviewPanel

/**
 * @author Marcin Pieciukiewicz
 */

class BooksPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookDatabase: BookDatabase = _

  val books = bookDatabase.getAllBooks


  add(new BookmarkablePageLink("yourBooksLink", classOf[YourBooksPage]))
  add(AuthorizeTrustedUser(new BookmarkablePageLink("addBookLink", classOf[AddBookPage])))

  add(new FindBookFormPanel("findBook"))


  add(new RepeatingView("book") {

    books.foreach(book => {

      add(new AbstractItem(newChildId()) {
        add(new BookSummaryPreviewPanel("bookSummaryPreview", book))
      })
    })
  })
}
