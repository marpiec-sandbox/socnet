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
import pl.marpiec.socnet.web.page.editUserProfilePage.addJobExperiencePanel.JobExperiencePanel
import pl.marpiec.socnet.web.page.editUserProfilePage.model.JobExperienceFormModel

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperienceListPanel(id: String, val user: User, val userProfile: UserProfile,
                             val jobExperience: ListBuffer[JobExperience]) extends Panel(id) {

  val userProfileCommand = Factory.userProfileCommand
  val uidGenerator = Factory.uidGenerator

  setOutputMarkupId(true)

  val jobExperienceList: RepeatingView = new RepeatingView("repeating") {
    for (experience <- jobExperience) {
      addExperienceToJobExperienceList(this, experience)
    }
  }

  def addExperienceToJobExperienceList(repeating: RepeatingView, experience: JobExperience): MarkupContainer = {
    val item: AbstractItem = new AbstractItem(repeating.newChildId());
    item.add(new JobExperiencePanel("content", user, userProfile, experience))
    repeating.add(item);
  }


  val newJobExperienceForm: SecureForm[JobExperienceFormModel] = new SecureForm[JobExperienceFormModel]("newJobExperienceForm") {
    setOutputMarkupPlaceholderTag(true)
    setVisible(false)

    setModel(new CompoundPropertyModel[JobExperienceFormModel](new JobExperienceFormModel))

    add(new TextField[String]("companyName"))
    add(new TextField[String]("position"))
    add(new TextArea[String]("description"))


    add(new SecureAjaxButton("cancelButton") {
      def onSecureSubmit(target: AjaxRequestTarget, form: Form[_]) {
        val model = form.getModel.asInstanceOf[CompoundPropertyModel[JobExperienceFormModel]].getObject
        model.clean()
        newJobExperienceForm.setVisible(false)
        showNewExperienceFormLink.setVisible(true)
        target.add(JobExperienceListPanel.this)
      }
    })

    add(new SecureAjaxButton("submitButton") {
      def onSecureSubmit(target: AjaxRequestTarget, form: Form[_]) {

        val model = form.getModel.asInstanceOf[CompoundPropertyModel[JobExperienceFormModel]].getObject

        val newExperienceId = uidGenerator.nextUid

        val experience = new JobExperience
        experience.companyName = model.companyName
        experience.description = model.description
        experience.position = model.position
        experience.id = newExperienceId

        userProfileCommand.addJobExperience(user.id, userProfile.id, userProfile.version, model.createJobExperienceParam, newExperienceId)
        userProfile.incrementVersion

        addExperienceToJobExperienceList(jobExperienceList, experience)

        model.clean()

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

}
