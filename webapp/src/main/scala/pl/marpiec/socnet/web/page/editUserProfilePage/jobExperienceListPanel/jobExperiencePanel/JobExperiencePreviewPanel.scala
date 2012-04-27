package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperienceListPanel.jobExperiencePanel

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.userprofile.JobExperience
import pl.marpiec.socnet.model.{User, UserProfile}
import pl.marpiec.socnet.web.page.editUserProfilePage.model.JobExperienceDateModel
import pl.marpiec.socnet.web.page.editUserProfilePage.jobExperienceListPanel.JobExperiencePanel

/**
 * @author Marcin Pieciukiewicz
 */

class JobExperiencePreviewPanel(id: String, parent: JobExperiencePanel, val jobExperience: JobExperience,
                                val userProfile: UserProfile, val user: User) extends Panel(id) {

  val userProfileCommand = Factory.userProfileCommand

  add(new Label("companyName", new PropertyModel[String](jobExperience, "companyName")))
  add(new Label("position", new PropertyModel[String](jobExperience, "position")))
  add(new Label("description", new PropertyModel[String](jobExperience, "description")))

  add(new Label("experienceDate", new JobExperienceDateModel(jobExperience)))


  add(new AjaxFallbackLink("editButton") {
    def onClick(target: AjaxRequestTarget) {
      parent.switchToEditMode
      target.add(parent)
    }
  })

  add(new AjaxFallbackLink("deleteButton") {
    def onClick(target: AjaxRequestTarget) {

      userProfileCommand.removeJobExperience(user.id, userProfile.id, userProfile.version, jobExperience.id)
      userProfile.incrementVersion
      parent.hideJobExperience
      target.add(parent)
    }
  })


}