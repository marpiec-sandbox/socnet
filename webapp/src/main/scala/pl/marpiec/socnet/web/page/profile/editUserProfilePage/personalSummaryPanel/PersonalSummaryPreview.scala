package pl.marpiec.socnet.web.page.profile.editUserProfilePage.personalSummaryPanel

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.web.page.profile.editUserProfilePage.PersonalSummaryPanel
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.link.ExternalLink

class PersonalSummaryPreview(id: String, userProfile: UserProfile, parent: PersonalSummaryPanel) extends WebMarkupContainer(id) {

  add(new Label("professionalTitle", new PropertyModel(userProfile, "professionalTitle")))
  add(new Label("city", new PropertyModel(userProfile, "city")))
  add(new Label("province", new PropertyModel(userProfile, "province.translation")))
  add(new ExternalLink("wwwPage", new PropertyModel[String](userProfile, "wwwPage"), new PropertyModel(userProfile, "wwwPage")))
  add(new ExternalLink("blogPage", new PropertyModel[String](userProfile, "blogPage"), new PropertyModel(userProfile, "blogPage")))
  add(new Label("summary", new PropertyModel(userProfile, "summary")))

  add(new AjaxFallbackLink("editButton") {
    def onClick(target: AjaxRequestTarget) {
      parent.switchToEditMode
      target.add(parent)
    }
  })
}
