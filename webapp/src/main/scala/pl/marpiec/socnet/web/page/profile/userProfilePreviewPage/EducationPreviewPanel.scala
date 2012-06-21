package pl.marpiec.socnet.web.page.profile.userProfilePreviewPage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.userprofile.Education
import org.apache.wicket.model.PropertyModel
import pl.marpiec.socnet.web.page.profile.editUserProfilePage.model.EducationDateIModel
import org.apache.wicket.markup.html.basic.{MultiLineLabel, Label}

/**
 * @author Marcin Pieciukiewicz
 */

class EducationPreviewPanel(id: String, education: Education) extends Panel(id) {
  add(new Label("schoolName", new PropertyModel[String](education, "schoolName")))
  add(new Label("faculty", new PropertyModel[String](education, "faculty")))
  add(new Label("major", new PropertyModel[String](education, "major")))
  add(new Label("level", new PropertyModel[String](education, "level")))
  add(new MultiLineLabel("description", new PropertyModel[String](education, "description")))

  add(new Label("educationDate", new EducationDateIModel(education)))
}
