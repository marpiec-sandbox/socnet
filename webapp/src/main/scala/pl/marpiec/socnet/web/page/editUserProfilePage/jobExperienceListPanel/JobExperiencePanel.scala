package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperienceListPanel

import scala.collection.JavaConversions._
import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.{UserProfile, User}
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}

import pl.marpiec.socnet.web.page.editUserProfilePage.model.{JobExperienceFormModelValidator, JobExperienceFormModel}
import org.apache.wicket.model.{PropertyModel, CompoundPropertyModel}
import org.apache.wicket.markup.html.form._
import socnet.constant.{Province, Month}

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
      add(new Label("warningMessage"))
      add(new TextField[String]("companyName"))
      add(new TextField[String]("position"))
      add(new TextArea[String]("description"))
      add(new CheckBox("currentJob"))
      add(new TextField[String]("fromYear"))
      add(new TextField[String]("toYear"))

      add(new DropDownChoice[Month]("fromMonth", Month.values, new ChoiceRenderer[Month]("translation")))
      add(new DropDownChoice[Month]("toMonth", Month.values, new ChoiceRenderer[Month]("translation")))

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


          val validationResult = JobExperienceFormModelValidator.validate(formModel)

          if (validationResult.isValid) {
            saveChangesToExperience(formModel)
            copyDataIntoJobExperienceAndIncrementVersion(formModel)

            edit = false
          } else {
            formModel.warningMessage = "Formularz nie zosta? wype?niony poprawnie"
          }


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
