package pl.marpiec.socnet.web.page.profile.userProfilePreviewPage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.userprofile.AdditionalInfo
import org.apache.wicket.model.PropertyModel
import pl.marpiec.socnet.web.page.profile.editUserProfilePage.model.AdditionalInfoDateIModel
import org.apache.wicket.markup.html.basic.{MultiLineLabel, Label}

/**
 * @author Marcin Pieciukiewicz
 */

class AdditionalInfoPreviewPanel(id: String, additionalInfo: AdditionalInfo) extends Panel(id) {
  add(new Label("title", new PropertyModel[String](additionalInfo, "title")))
  add(new MultiLineLabel("description", new PropertyModel[String](additionalInfo, "description")))

  add(new Label("additionalInfoDate", new AdditionalInfoDateIModel(additionalInfo)))
}
