package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperienceListPanel

import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.page.editUserProfilePage.model.JobExperienceFormModel
import pl.marpiec.util.UID
import org.apache.wicket.Component
import pl.marpiec.socnet.web.page.editUserProfilePage.JobExperienceListPanel
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.{UserProfile, User}
import org.apache.wicket.markup.html.panel.{EmptyPanel, Panel}

/**
 * @author Marcin Pieciukiewicz
 */

class JobExperienceAdditionPanel(id: String, val parent: JobExperienceListPanel, val user: User, val userProfile: UserProfile) extends Panel(id) {

  //dependencies
  val userProfileCommand = Factory.userProfileCommand
  val uidGenerator = Factory.uidGenerator

  setOutputMarkupId(true)
  setOutputMarkupPlaceholderTag(true)
  setVisible(false)

  add(new EmptyPanel("jobExperiencePanel"))
  add(new EmptyPanel("jobExperienceAdditionPanel"))

  val addJobExperienceForm: JobExperienceFormPanel = addAndReturn(new JobExperienceFormPanel("addForm", true, new JobExperience) {

    setVisible(true)

    def onFormSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
      val experience = saveNewExperience(formModel)
      showAddedExperience(experience)
      formModel.clear()
      parent.setNewJobExperienceAdditionPanel(addAndReturn(new JobExperienceAdditionPanel("jobExperienceAdditionPanel", parent, user, userProfile)))
      this.setVisible(false)
      parent.hideAddExperienceForm
      target.add(JobExperienceAdditionPanel.this)
      target.add(parent.showNewExperienceFormLink)
    }

    def onFormCanceled(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
      formModel.clear()
      parent.hideAddExperienceForm
      target.add(JobExperienceAdditionPanel.this)
      target.add(parent.showNewExperienceFormLink)
    }
  })
  
  def showAddedExperience(experience: JobExperience) {
    addOrReplace(new JobExperiencePanel("jobExperiencePanel", user, userProfile, experience))
  }

  def saveNewExperience(formModel: JobExperienceFormModel):JobExperience = {
    val newExperienceId = uidGenerator.nextUid

    saveNewExperienceAndIncrementProfileVersion(formModel, newExperienceId)

    val experience = new JobExperience
    JobExperienceFormModel.copy(experience, formModel)
    experience.id = newExperienceId
    experience
  }



  def saveNewExperienceAndIncrementProfileVersion(formModel: JobExperienceFormModel, newExperienceId: UID) {
    userProfileCommand.addJobExperience(user.id, userProfile.id, userProfile.version, formModel.createJobExperienceParam, newExperienceId)
    userProfile.incrementVersion
  }


  private def addAndReturn[E <: Component](child: E): E = {
    addOrReplace(child)
    child
  }


}
