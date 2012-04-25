package pl.marpiec.socnet.web.page.editUserProfilePage.personalSummaryPanel

import pl.marpiec.socnet.model.{User, UserProfile}
import pl.marpiec.socnet.web.page.editUserProfilePage.PersonalSummaryPanel
import pl.marpiec.socnet.web.page.editUserProfilePage.model.PersonalSummaryFormModel
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import pl.marpiec.socnet.di.Factory
import org.apache.wicket.markup.html.form.{TextArea, TextField}
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.{Model, CompoundPropertyModel}
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator
import org.apache.wicket.Component


class PersonalSummaryEditForm(id: String, userProfile: UserProfile, user: User, parent: PersonalSummaryPanel) extends SecureForm[PersonalSummaryFormModel](id) {

  val userProfileCommand: UserProfileCommand = Factory.userProfileCommand

  val warningMessage = new Model[String]("")
  
  def initialize {
    val model = PersonalSummaryFormModel(userProfile)
    setModel(new CompoundPropertyModel[PersonalSummaryFormModel](model))
    setVisible(false)
  }

  def buildSchema {

   // add(new Label("warningMessage", warningMessage))
    add(new TextField[String]("professionalTitle"))
    add(new TextField[String]("city"))
    add(new TextField[String]("province"))
    add(new TextField[String]("wwwPage"))
    add(new TextField[String]("blogPage"))
    add(new TextArea[String]("summary"))

    addCancelButton
    addSubmitButton

  }


  def addCancelButton {
    add(new SecureAjaxButton[PersonalSummaryFormModel]("cancelButton") {
      def onSecureSubmit(target: AjaxRequestTarget, formModel: PersonalSummaryFormModel) {
        revertFormData(formModel)
        parent.switchToPreviewMode
        target.add(parent)
      }
    })
  }

  def addSubmitButton {
    add(new SecureAjaxButton[PersonalSummaryFormModel]("submitButton") {


      override def getAjaxCallDecorator = new AjaxCallDecorator() {
        override def decorateScript(c: Component, script: CharSequence): CharSequence = {
          "if (!validateForm(jQuery(this).parents(\"form\"))) return false;" + script;
        }
      }

      override def onSecureSubmit(target: AjaxRequestTarget, formModel: PersonalSummaryFormModel) {
        saveChangesToUserProfile(formModel)
        copyFormDataIntoUserProfileAndIncrementVersion(formModel)

        parent.switchToPreviewMode
        target.add(parent)
      }
    })
  }

  def revertFormData(formModel: PersonalSummaryFormModel) {
    PersonalSummaryFormModel.copy(formModel, userProfile)
  }

  def copyFormDataIntoUserProfileAndIncrementVersion(form: PersonalSummaryFormModel) {
    PersonalSummaryFormModel.copy(userProfile, form)
    userProfile.incrementVersion
  }

  def saveChangesToUserProfile(personalSummary: PersonalSummaryFormModel) {
    userProfileCommand.updatePersonalSummary(user.id, userProfile.id, userProfile.version, personalSummary.createPersonalSummary)
  }
}
