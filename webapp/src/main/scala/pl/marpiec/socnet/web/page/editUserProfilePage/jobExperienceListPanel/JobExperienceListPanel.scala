package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperienceListPanel

import org.apache.wicket.markup.html.panel.Panel
import collection.mutable.ListBuffer
import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.{TextArea, TextField, Form}
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.MarkupContainer
import pl.marpiec.socnet.model.{UserProfile, User}
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import pl.marpiec.socnet.web.page.editUserProfilePage.jobExperiencePanel.JobExperiencePanel
import pl.marpiec.socnet.web.page.editUserProfilePage.model.JobExperienceFormModel
import pl.marpiec.util.UID

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
  val jobExperienceList = new RepeatingView("repeating") {
    for (experience <- jobExperience) {
      addExperience(experience)
    }

    def addExperience(experience: JobExperience): MarkupContainer = {
      val item: AbstractItem = new AbstractItem(this.newChildId());
      item.add(new JobExperiencePanel("content", user, userProfile, experience))
      this.add(item);
    }
  }


  val newJobExperienceForm: SecureForm[JobExperienceFormModel] = new SecureForm[JobExperienceFormModel]("newJobExperienceForm") {
    setOutputMarkupPlaceholderTag(true)
    setVisible(false)

    setModel(new CompoundPropertyModel[JobExperienceFormModel](new JobExperienceFormModel))

    add(new TextField[String]("companyName"))
    add(new TextField[String]("position"))
    add(new TextArea[String]("description"))


    add(new SecureAjaxButton[JobExperienceFormModel]("cancelButton") {
      def onSecureSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
        formModel.clear()
        newJobExperienceForm.setVisible(false)
        showNewExperienceFormLink.setVisible(true)
        target.add(JobExperienceListPanel.this)
      }
    })

    add(new SecureAjaxButton[JobExperienceFormModel]("submitButton") {
      def onSecureSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {

        val newExperienceId = uidGenerator.nextUid

        saveNewExperienceAndIncrementProfileVersion(formModel, newExperienceId)
        addNewExperienceToList(formModel, newExperienceId)
        clearFormData(formModel)

        newJobExperienceForm.setVisible(false)
        showNewExperienceFormLink.setVisible(true)
        target.add(JobExperienceListPanel.this)
      }
    })
  }

  val showNewExperienceFormLink: AjaxLink[String] = new AjaxLink[String]("showNewExperienceFormLink") {
    setOutputMarkupId(true)

    def onClick(target: AjaxRequestTarget) {
      newJobExperienceForm.setVisible(true)
      this.setVisible(false)
      target.add(newJobExperienceForm)
      target.add(this)
    }
  }

  add(jobExperienceList)
  add(newJobExperienceForm)
  add(showNewExperienceFormLink)



  //methods
  def clearFormData(formModel: JobExperienceFormModel) {
    formModel.clear()
  }

  def addNewExperienceToList(formModel: JobExperienceFormModel, newExperienceId: UID) {
    val experience = new JobExperience
    JobExperienceFormModel.copy(experience, formModel)
    experience.id = newExperienceId

    jobExperienceList.addExperience(experience)
  }

  def saveNewExperienceAndIncrementProfileVersion(formModel: JobExperienceFormModel, newExperienceId: UID) {
    userProfileCommand.addJobExperience(user.id, userProfile.id, userProfile.version, formModel.createJobExperienceParam, newExperienceId)
    userProfile.incrementVersion
  }

}
