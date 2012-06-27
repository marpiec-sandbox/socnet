package pl.marpiec.socnet.web.page.profile.editUserProfilePage.personalSummaryPanel

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.web.page.profile.editUserProfilePage.PersonalSummaryPanel
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.link.ExternalLink
import pl.marpiec.socnet.web.component.wicket.LabelInvisibleWhenEmpty
import org.apache.commons.lang.StringUtils
import org.apache.wicket.model.PropertyModel
import pl.marpiec.util.UrlUtil

class PersonalSummaryPreview(id: String, userProfile: UserProfile, parent: PersonalSummaryPanel) extends WebMarkupContainer(id) {

  add(new LabelInvisibleWhenEmpty("city", new PropertyModel(userProfile, "city")))
  add(new LabelInvisibleWhenEmpty("province", new PropertyModel(userProfile, "province.translation")))

  addWwwLink("wwwPage", userProfile, "wwwPage")
  addWwwLink("blogPage", userProfile, "blogPage")

  add(new LabelInvisibleWhenEmpty("summary", new PropertyModel(userProfile, "summary")))

  add(new AjaxFallbackLink("editButton") {
    def onClick(target: AjaxRequestTarget) {
      parent.switchToEditMode
      target.add(parent)
    }
  })

  private def addWwwLink(id: String, model: AnyRef, property: String) {
    add(new ExternalLink(id,
      new PropertyModel[String](model, property) {
        override def getObject = {
          if (super.getObject == null) {
            null
          } else {
            UrlUtil.addHttpIfNoProtocol(super.getObject)
          }
        }
      },
      new PropertyModel[String](model, property) {
        override def getObject = {
          if (super.getObject == null) {
            null
          } else {
            UrlUtil.removeProtocol(super.getObject)
          }
        }
      }) {
      override def isVisible = super.isVisible && StringUtils.isNotBlank(getDefaultModelObjectAsString)
    })
  }

}
