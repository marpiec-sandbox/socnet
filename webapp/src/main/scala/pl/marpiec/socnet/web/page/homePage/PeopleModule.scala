package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import people.PeopleDashboardPanel
import pl.marpiec.socnet.web.authorization.AuthorizeUser

/**
 * @author Marcin Pieciukiewicz
 */

class PeopleModule(id:String) extends Panel(id) {


  add(AuthorizeUser(new PeopleDashboardPanel("peopleDashboard")))

}
