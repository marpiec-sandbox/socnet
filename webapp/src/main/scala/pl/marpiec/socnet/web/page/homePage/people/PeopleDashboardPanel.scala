package pl.marpiec.socnet.web.page.homePage.people

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.form.TextField
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.web.page.FindPeoplePage
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.wicket.SimpleStatelessForm

/**
 * @author Marcin Pieciukiewicz
 */

class PeopleDashboardPanel(id: String) extends Panel(id) {

  add(new SimpleStatelessForm("findPeopleForm") {
    var query: String = _


    add(new TextField[String]("query"))

    override def onSubmit() {
      if (StringUtils.isNotEmpty(query)) {
        setResponsePage(classOf[FindPeoplePage], new PageParameters().add(FindPeoplePage.QUERY_PARAM, query))
      }
    }
  })

}
