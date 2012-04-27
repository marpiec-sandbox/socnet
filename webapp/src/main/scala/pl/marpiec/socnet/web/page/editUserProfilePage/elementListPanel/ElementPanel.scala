package pl.marpiec.socnet.web.page.editUserProfilePage.elementListPanel

import pl.marpiec.socnet.web.page.editUserProfilePage.jobExperience.{JobExperienceFormPanel, JobExperiencePreviewPanel}
import org.apache.wicket.markup.html.panel.Panel

import pl.marpiec.socnet.model.{User, UserProfile}
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.page.editUserProfilePage.model.{JobExperienceFormModelValidator, JobExperienceFormModel}
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.userprofile.JobExperience

/**
 * ...
 * @author Marcin Pieciukiewicz
 */


class ElementPanel[T](id: String, val user: User, val userProfile: UserProfile, val element: T)
  extends Panel(id) {

  //dependencies
  val userProfileCommand = Factory.userProfileCommand

  //configure
  setOutputMarkupId(true)

  //schema
  val previewPanel = addPreviewPanel
  val editForm = addEditForm


  //methods

  def addPreviewPanel():JobExperiencePreviewPanel = {
    addAndReturn(new JobExperiencePreviewPanel("elementPreview", this, element.asInstanceOf[JobExperience], userProfile, user))
  }

  def addEditForm:JobExperienceFormPanel = {
    addAndReturn(new JobExperienceFormPanel("elementForm", false, element.asInstanceOf[JobExperience]) {
      def onFormSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) = {

        formModel.warningMessage = ""
        val validationResult = JobExperienceFormModelValidator.validate(formModel)

        if (validationResult.isValid) {
          saveChangesToElement(formModel)
          copyDataIntoElementAndIncrementProfileVersion(formModel)
          switchToPreviewMode
        } else {
          formModel.warningMessage = "Formularz nie zostal wypelniony poprawnie"
        }

        target.add(ElementPanel.this)
      }

      def onFormCanceled(target: AjaxRequestTarget, formModel: JobExperienceFormModel) = {
        revertFormData(formModel)
        switchToPreviewMode
        target.add(ElementPanel.this)
      }
    })
  }


  def revertFormData(formModel: JobExperienceFormModel) {
    JobExperienceFormModel.copy(formModel, element.asInstanceOf[JobExperience])
  }

  def saveChangesToElement(formModel: JobExperienceFormModel) {
    userProfileCommand.updateJobExperience(user.id, userProfile.id, userProfile.version, formModel.createJobExperienceParam)
  }


  def copyDataIntoElementAndIncrementProfileVersion(formModel: JobExperienceFormModel) {
    JobExperienceFormModel.copy(element.asInstanceOf[JobExperience], formModel)
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
