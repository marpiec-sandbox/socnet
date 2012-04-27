package pl.marpiec.socnet.web.page.editUserProfilePage.elementListPanel

import pl.marpiec.socnet.web.page.editUserProfilePage.jobExperience.JobExperienceFormPanel
import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.util.UID
import org.apache.wicket.Component
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.{UserProfile, User}
import org.apache.wicket.markup.html.panel.{EmptyPanel, Panel}
import pl.marpiec.socnet.web.page.editUserProfilePage.model.{JobExperienceFormModelValidator, JobExperienceFormModel}

/**
 * @author Marcin Pieciukiewicz
 */

class ElementAdditionPanel[T](id: String, val parent: ElementListPanel[T], val user: User, val userProfile: UserProfile) extends Panel(id) {

  //dependencies
  val userProfileCommand = Factory.userProfileCommand
  val uidGenerator = Factory.uidGenerator

  setOutputMarkupId(true)
  setOutputMarkupPlaceholderTag(true)
  setVisible(false)

  add(new EmptyPanel("elementPanel"))
  add(new EmptyPanel("elementAdditionPanel"))

  val addElementForm: JobExperienceFormPanel = addAndReturn(
    new JobExperienceFormPanel("addElementForm", true, new JobExperience) {

      setVisible(true)

      def onFormSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {

        formModel.warningMessage = ""
        val validationResult = JobExperienceFormModelValidator.validate(formModel)

        if (validationResult.isValid) {
          val experience = saveNewExperience(formModel)
          showAddedExperience(experience)
          changeCurrentJobExperienceAdditionPanel

          formModel.clear()
          this.setVisible(false)
          parent.hideAddElementForm
          target.add(parent.showNewElementFormLink)

        } else {
          formModel.warningMessage = "Formularz nie zostal wypelniony poprawnie"
        }

        target.add(ElementAdditionPanel.this)

      }

      def onFormCanceled(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
        formModel.clear()
        parent.hideAddElementForm
        target.add(ElementAdditionPanel.this)
        target.add(parent.showNewElementFormLink)
      }
    })

  def changeCurrentJobExperienceAdditionPanel {
    parent.changeCurrentElementAdditionPanel(addAndReturn(new ElementAdditionPanel("jobExperienceAdditionPanel", parent, user, userProfile)))
  }

  def showAddedExperience(experience: JobExperience) {
    addOrReplace(new ElementPanel("jobExperiencePanel", user, userProfile, experience))
  }

  def saveNewExperience(formModel: JobExperienceFormModel): JobExperience = {
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
