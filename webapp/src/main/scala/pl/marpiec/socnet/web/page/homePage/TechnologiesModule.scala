package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.usertechnologies.UserTechnologiesPage
import pl.marpiec.socnet.web.authorization.AuthorizeUser

/**
 * @author Marcin Pieciukiewicz
 */

class TechnologiesModule(id:String) extends Panel(id) {

  add(AuthorizeUser(new BookmarkablePageLink("technologiesLink", classOf[UserTechnologiesPage])))


}
