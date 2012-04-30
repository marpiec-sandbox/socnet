package pl.marpiec.socnet.web.page.userProfilePreviewPage

import pl.marpiec.socnet.model.User
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class UserPreviewPanel(id: String, val user: User) extends Panel(id) {

  //schema
  add(new Label("userName", user.fullName))

}
