package pl.marpiec.socnet.web.page.editUserProfilePage.personalSummaryPanel

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.web.page.editUserProfilePage.PersonalSummaryPanel
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget

class PersonalSummaryPreview(id: String, userProfile: UserProfile, parent: PersonalSummaryPanel) extends WebMarkupContainer("personalSummaryPreview") {

  add(new Label("professionalTitle", new PropertyModel(userProfile, "professionalTitle")))
  add(new Label("city", new PropertyModel(userProfile, "city")))
  add(new Label("province", new PropertyModel(userProfile, "province")))
  add(new Label("wwwPage", new PropertyModel(userProfile, "wwwPage")))
  add(new Label("blogPage", new PropertyModel(userProfile, "blogPage")))
  add(new Label("summary", new PropertyModel(userProfile, "summary")))

  add(new AjaxFallbackLink("editButton") {
    def onClick(target: AjaxRequestTarget) {
      parent.switchToEditMode
      target.add(parent)
    }
  })
}
