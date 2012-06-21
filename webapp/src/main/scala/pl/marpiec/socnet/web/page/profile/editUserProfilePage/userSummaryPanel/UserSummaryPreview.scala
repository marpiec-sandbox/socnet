package pl.marpiec.socnet.web.page.profile.editUserProfilePage.userSummaryPanel

import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.page.profile.editUserProfilePage.UserSummaryPanel
import pl.marpiec.socnet.model.User

class UserSummaryPreview(id: String, user: User, parent: UserSummaryPanel) extends WebMarkupContainer(id) {

  add(new Label("userName", new PropertyModel(user, "fullName")))
  add(new Label("userSummary", new PropertyModel(user, "summary")))

  add(new AjaxFallbackLink("editButton") {
    def onClick(target: AjaxRequestTarget) {
      parent.switchToEditMode
      target.add(parent)
    }
  })
}
