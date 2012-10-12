package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.authorization.AuthorizeUser
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.BookDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.books.{YourBooksPage, BooksPage}
import pl.marpiec.socnet.web.component.book.{FindBookFormPanel, SimpleBookSummaryPreviewPanel}

/**
 * @author Marcin Pieciukiewicz
 */

class BooksModule(id: String) extends Panel(id) {

  @SpringBean private var bookDatabase: BookDatabase = _

  val recentBooks = bookDatabase.getAllBooks

  add(AuthorizeUser(new BookmarkablePageLink("yourBooksLink", classOf[YourBooksPage])))
  add(AuthorizeUser(new BookmarkablePageLink("booksLink", classOf[BooksPage])))

  add(new FindBookFormPanel("findBookFormPanel"))


  add(new RepeatingView("bestBooks") {
    recentBooks.foreach(book => {
      add(new AbstractItem(newChildId()) {
        add(new SimpleBookSummaryPreviewPanel("simpleBookSummaryPreview", book))
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
