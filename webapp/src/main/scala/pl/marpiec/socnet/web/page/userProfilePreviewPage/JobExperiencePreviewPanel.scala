package pl.marpiec.socnet.web.page.userProfilePreviewPage

import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.PropertyModel
import pl.marpiec.socnet.web.page.editUserProfilePage.model.JobExperienceDateIModel
import org.apache.wicket.markup.html.basic.{MultiLineLabel, Label}

/**
 * @author Marcin Pieciukiewicz
 */

class JobExperiencePreviewPanel(id:String, jobExperience:JobExperience) extends Panel(id) {
  add(new Label("companyName", new PropertyModel[String](jobExperience, "companyName")))
  add(new Label("position", new PropertyModel[String](jobExperience, "position")))
  add(new MultiLineLabel("description", new PropertyModel[String](jobExperience, "description")))

  add(new Label("experienceDate", new JobExperienceDateIModel(jobExperience)))
}
