package pl.marpiec.socnet.web.page.books

import component.{BooksLinks, BooksLinksPanel}
import pl.marpiec.socnet.constant.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.readdatabase.{BookReviewsDatabase, BookDatabase}
import pl.marpiec.socnet.redundandmodel.book.BookReviews
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.Book
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.web.component.book.BookSummaryPreviewPanel

/**
 * @author Marcin Pieciukiewicz
 */

object BooksPage {
  val QUERY_PARAM = "query"
}

class BooksPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var bookDatabase: BookDatabase = _
  @SpringBean private var bookReviewsDatabase: BookReviewsDatabase = _

  val query = parameters.get(BooksPage.QUERY_PARAM).toString

  val books = findBooksByQuery(query)
  val booksReviews = bookReviewsDatabase.getBooksReviewsForBooksIds(convertToIdsList(books))

  add(new BooksLinksPanel("booksLinksPanel", BooksLinks.ALL_BOOKS_LINKS))

  add(new RepeatingView("book") {

    books.foreach(book => {
      val bookReviews = booksReviews.get(book.id).getOrElse(new BookReviews)

      add(new AbstractItem(newChildId()) {
        add(new BookSummaryPreviewPanel("bookSummaryPreview", book, bookReviews))
      })
    })
  })


  private def convertToIdsList(books: List[Book]): List[UID] = {
    books.map(book => book.id)
  }

  private def findBooksByQuery(query: String): List[Book] = {

    if (StringUtils.isBlank(query)) {
      bookDatabase.getAllBooks
    } else {
      bookDatabase.findBooksByQuery(query)
    }

  }
}
