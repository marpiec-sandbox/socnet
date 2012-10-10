package pl.marpiec.socnet.web.page.books

import booksPage.FindBookFormPanel
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.authorization.{AuthorizeTrustedUser, AuthorizeUser, SecureWebPage}
import yourBooksPage.BookPreviewWithOwnershipPanel
import pl.marpiec.socnet.redundandmodel.book.BookReviews
import pl.marpiec.socnet.readdatabase.{BookUserInfoDatabase, BookReviewsDatabase, BookDatabase}

/**
 * @author Marcin Pieciukiewicz
 */

class YourBooksPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookDatabase: BookDatabase = _
  @SpringBean private var bookReviewsDatabase: BookReviewsDatabase = _
  @SpringBean private var bookUserInfoDatabase: BookUserInfoDatabase = _

  val booksIds = bookUserInfoDatabase.getBooksOwnedBy(session.userId)

  val books = bookDatabase.getBooksByIds(booksIds)

  add(AuthorizeUser(new BookmarkablePageLink("booksPageLink", classOf[BooksPage])))

  add(new FindBookFormPanel("findBook"))

  add(new RepeatingView("book") {

    books.foreach(book => {

      val bookUserInfoOption = bookUserInfoDatabase.getUserInfoByUserAndBook(session.userId, book.id)

      add(new AbstractItem(newChildId()) {
        add(new BookPreviewWithOwnershipPanel("bookPreviewWithOwnershipPanel", book, bookUserInfoOption))
      })
    })
  })
}
