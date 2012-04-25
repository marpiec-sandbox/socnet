package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperienceListPanel

import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.{TextArea, TextField}
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.{UserProfile, User}
import pl.marpiec.socnet.web.page.editUserProfilePage.model.JobExperienceFormModel
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import org.apache.wicket.model.{PropertyModel, CompoundPropertyModel}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperiencePanel(id: String, val user: User, val userProfile: UserProfile, val jobExperience: JobExperience)
  extends Panel(id) {

  val userProfileCommand = Factory.userProfileCommand

  var edit = false

  setOutputMarkupId(true)

  add(new WebMarkupContainer("experiencePreview") {
    add(new Label("companyName", new PropertyModel[String](jobExperience, "companyName")))
    add(new Label("position", new PropertyModel[String](jobExperience, "position")))
    add(new Label("description", new PropertyModel[String](jobExperience, "description")))

    add(new AjaxFallbackLink("editButton") {
      def onClick(target: AjaxRequestTarget) {
        edit = true
        target.add(JobExperiencePanel.this)
      }
    })

    add(new AjaxFallbackLink("deleteButton") {
      def onClick(target: AjaxRequestTarget) {

        userProfileCommand.removeJobExperience(user.id, userProfile.id, userProfile.version, jobExperience.id)
        userProfile.incrementVersion
        JobExperiencePanel.this.setVisible(false)
        target.add(JobExperiencePanel.this)
      }
    })

    override def onConfigure() {
      setVisible(!edit)
    }
  })


  add(new SecureForm[JobExperienceFormModel]("experienceForm") {


    def initialize() {
      val formModel = JobExperienceFormModel(jobExperience)
      setModel(new CompoundPropertyModel[JobExperienceFormModel](formModel))
    }


    def buildSchema() {
      add(new TextField[String]("companyName"))
      add(new TextField[String]("position"))
      add(new TextArea[String]("description"))

      addCancelButton
      addSubmitButton
    }

    override def onConfigure() {
      setVisible(edit)
    }

    def addCancelButton {
      add(new SecureAjaxButton[JobExperienceFormModel]("cancelButton") {
        def onSecureSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
          revertFormData(formModel)
          edit = false
          target.add(JobExperiencePanel.this)
        }
      })
    }

    def addSubmitButton {
      add(new SecureAjaxButton[JobExperienceFormModel]("submitButton") {
        def onSecureSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {

          saveChangesToExperience(formModel)
          copyDataIntoJobExperienceAndIncrementVersion(formModel)

          edit = false
          target.add(JobExperiencePanel.this)
        }
      })
    }


  })


  def copyDataIntoJobExperienceAndIncrementVersion(formModel: JobExperienceFormModel) {
    JobExperienceFormModel.copy(jobExperience, formModel)
    userProfile.incrementVersion
  }

  def saveChangesToExperience(formModel: JobExperienceFormModel) {
    userProfileCommand.updateJobExperience(user.id, userProfile.id, userProfile.version, formModel.createJobExperienceParam)
  }

  def revertFormData(formModel: JobExperienceFormModel) {
    JobExperienceFormModel.copy(formModel, jobExperience)
  };

}
