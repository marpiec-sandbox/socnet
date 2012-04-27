package pl.marpiec.socnet.web.page.editUserProfilePage

import jobExperienceListPanel.{JobExperienceFormPanel, AddJobExperienceForm, JobExperiencePanel}
import org.apache.wicket.markup.html.panel.Panel
import collection.mutable.ListBuffer
import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.model.{UserProfile, User}
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.web.page.editUserProfilePage.model.JobExperienceFormModel
import pl.marpiec.util.UID
import org.apache.wicket.{Component, MarkupContainer}


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperienceListPanel(id: String, val user: User, val userProfile: UserProfile,
                             val jobExperience: ListBuffer[JobExperience]) extends Panel(id) {

  //dependencies
  val userProfileCommand = Factory.userProfileCommand
  val uidGenerator = Factory.uidGenerator

  //configure
  setOutputMarkupId(true)


  //schema
  val jobExperienceList = addJobExperienceList
  val addJobExperienceForm:JobExperienceFormPanel = addAndReturn(new JobExperienceFormPanel("addJobExperienceForm", true, new JobExperience) {
    def onFormSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
      saveNewExperience(formModel)

      formModel.clear()
      hideAddExperienceForm
      target.add(JobExperienceListPanel.this)
    }

    def onFormCanceled(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
      formModel.clear()
      hideAddExperienceForm
      target.add(addJobExperienceForm)
      target.add(showNewExperienceFormLink)
    }
  })
  val showNewExperienceFormLink = addShowNewExperienceFormLink

  //methods

  def addShowNewExperienceFormLink(): AjaxLink[String] = {
    addAndReturn(new AjaxLink[String]("showNewExperienceFormLink") {
      setOutputMarkupId(true)
      setOutputMarkupPlaceholderTag(true)

      def onClick(target: AjaxRequestTarget) {
        showAddExperienceForm()
        target.add(addJobExperienceForm)
        target.add(this)
      }
    })
  }

  def hideAddExperienceForm() {
    addJobExperienceForm.setVisible(false)
    showNewExperienceFormLink.setVisible(true)
  }

  def showAddExperienceForm() {
    addJobExperienceForm.setVisible(true)
    showNewExperienceFormLink.setVisible(false)
  }

  def saveNewExperience(formModel: JobExperienceFormModel) {
    val newExperienceId = uidGenerator.nextUid

    saveNewExperienceAndIncrementProfileVersion(formModel, newExperienceId)
    addNewExperienceToList(formModel, newExperienceId)
  }

  def addNewExperienceToList(formModel: JobExperienceFormModel, newExperienceId: UID) {
    val experience = new JobExperience
    JobExperienceFormModel.copy(experience, formModel)
    experience.id = newExperienceId

    addExperience(jobExperienceList, experience)
  }

  def saveNewExperienceAndIncrementProfileVersion(formModel: JobExperienceFormModel, newExperienceId: UID) {
    userProfileCommand.addJobExperience(user.id, userProfile.id, userProfile.version, formModel.createJobExperienceParam, newExperienceId)
    userProfile.incrementVersion
  }

  def addExperience(experienceList:RepeatingView, experience: JobExperience): MarkupContainer = {
    val item: AbstractItem = new AbstractItem(experienceList.newChildId());
    item.add(new JobExperiencePanel("content", user, userProfile, experience))
    experienceList.add(item);
  }

  def addJobExperienceList(): RepeatingView = {
    addAndReturn(new RepeatingView("repeating") {
      for (experience <- jobExperience) {
        addExperience(this, experience)
      }
    })
  }

  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }

}
