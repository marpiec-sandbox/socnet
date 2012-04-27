package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperienceListPanel

import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.html.panel.Panel

import pl.marpiec.socnet.model.{User, UserProfile}
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.page.editUserProfilePage.model.{JobExperienceFormModelValidator, JobExperienceFormModel}
import pl.marpiec.socnet.di.Factory

/**
 * ...
 * @author Marcin Pieciukiewicz
 */


class JobExperiencePanel(id: String, val user: User, val userProfile: UserProfile, val jobExperience: JobExperience)
  extends Panel(id) {

  val userProfileCommand = Factory.userProfileCommand

  //sonfigure
  setOutputMarkupId(true)


  //schema
  val previewPanel = addAndReturn(new JobExperiencePreviewPanel("experiencePreview", this, jobExperience, userProfile, user))
  val editForm:JobExperienceFormPanel = addAndReturn(new JobExperienceFormPanel("experienceForm", false, jobExperience) {
    def onFormSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) = {
      val validationResult = JobExperienceFormModelValidator.validate(formModel)

      if (validationResult.isValid) {
        saveChangesToExperience(formModel)
        copyDataIntoJobExperienceAndIncrementVersion(formModel)

        switchToPreviewMode
        formModel.warningMessage = ""
      } else {
        formModel.warningMessage = "Formularz nie zostal wypelniony poprawnie"
      }

      target.add(JobExperiencePanel.this)
    }

    def onFormCanceled(target: AjaxRequestTarget, formModel: JobExperienceFormModel) = {
      revertFormData(formModel)
      switchToPreviewMode
      target.add(JobExperiencePanel.this)
    }
  })


  //methods

  def revertFormData(formModel: JobExperienceFormModel) {
    JobExperienceFormModel.copy(formModel, jobExperience)
  }

  def saveChangesToExperience(formModel: JobExperienceFormModel) {
    userProfileCommand.updateJobExperience(user.id, userProfile.id, userProfile.version, formModel.createJobExperienceParam)
  }


  def copyDataIntoJobExperienceAndIncrementVersion(formModel: JobExperienceFormModel) {
    JobExperienceFormModel.copy(jobExperience, formModel)
    userProfile.incrementVersion
  }

  def switchToPreviewMode {
    editForm.setVisible(false)
    previewPanel.setVisible(true)
  }

  def switchToEditMode {
    editForm.setVisible(true)
    previewPanel.setVisible(false)
  }

  def hideJobExperience {
    this.setVisible(false)
  }


  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }


}
