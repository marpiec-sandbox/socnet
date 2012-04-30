package pl.marpiec.socnet.web.page.userProfilePreviewPage

import org.apache.wicket.markup.html.panel.Panel
import socnet.model.userprofile.AdditionalInfo
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.PropertyModel
import pl.marpiec.socnet.web.page.editUserProfilePage.model.AdditionalInfoDateIModel

/**
 * @author Marcin Pieciukiewicz
 */

class AdditionalInfoPreviewPanel(id: String, additionalInfo: AdditionalInfo) extends Panel(id) {
  add(new Label("title", new PropertyModel[String](additionalInfo, "title")))
  add(new Label("description", new PropertyModel[String](additionalInfo, "description")))

  add(new Label("additionalInfoDate", new AdditionalInfoDateIModel(additionalInfo)))
}
