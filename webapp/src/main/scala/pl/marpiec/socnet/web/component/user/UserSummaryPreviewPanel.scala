package pl.marpiec.socnet.web.component.user

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class UserSummaryPreviewPanel(id:String, user:User) extends Panel(id) {

  add(UserProfilePreviewPage.getLink("profileLink", user)
    .add(new Label("userName", user.fullName))
    .add(new Label("summary", user.summary)))

}
