package pl.marpiec.socnet.web.component.book

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.wicket.SimpleStatelessForm
import org.apache.wicket.markup.html.form.TextField
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.web.page.FindPeoplePage
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.page.books.BooksPage

/**
 * @author Marcin Pieciukiewicz
 */

class FindBookFormPanel(id: String) extends Panel(id) {

  add(new SimpleStatelessForm("findBookForm") {
    var query: String = _

    add(new TextField[String]("query"))

    override def onSubmit() {
      if (StringUtils.isNotEmpty(query)) {
        setResponsePage(classOf[BooksPage], new PageParameters().add(FindPeoplePage.QUERY_PARAM, query))
      }
    }
  })

}
