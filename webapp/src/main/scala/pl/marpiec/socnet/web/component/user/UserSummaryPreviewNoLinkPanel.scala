package pl.marpiec.socnet.web.component.user

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.User
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class UserSummaryPreviewNoLinkPanel(id: String, user: User) extends Panel(id) {

  add(new Label("userName", user.fullName))
  add(new Label("summary", user.summary))

}
