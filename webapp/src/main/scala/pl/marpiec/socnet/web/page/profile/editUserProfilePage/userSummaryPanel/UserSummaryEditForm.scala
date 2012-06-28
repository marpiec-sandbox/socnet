package pl.marpiec.socnet.web.page.profile.editUserProfilePage.userSummaryPanel

import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.web.page.profile.editUserProfilePage.model.{UserSummaryFormModelValidator, UserSummaryFormModel}
import pl.marpiec.socnet.web.page.profile.editUserProfilePage.UserSummaryPanel
import pl.marpiec.socnet.service.user.UserCommand


class UserSummaryEditForm(id: String, user: User, parent: UserSummaryPanel) extends SecureForm[UserSummaryFormModel](id) {

  @SpringBean private var userCommand: UserCommand = _

  def initialize {
    val model = UserSummaryFormModel(user)
    setModel(new CompoundPropertyModel[UserSummaryFormModel](model))
    setVisible(false)
  }

  def buildSchema {

    add(new Label("warningMessage"))
    add(new TextField[String]("firstName"))
    add(new TextField[String]("lastName"))
    add(new TextField[String]("summary"))

    addCancelButton
    addSubmitButton
  }


  def addCancelButton {
    add(new SecureAjaxButton[UserSummaryFormModel]("cancelButton") {
      def onSecureSubmit(target: AjaxRequestTarget, formModel: UserSummaryFormModel) {
        revertFormData(formModel)
        parent.switchToPreviewMode
        target.add(parent)
      }
    })
  }

  def addSubmitButton {
    add(new SecureAjaxButton[UserSummaryFormModel]("submitButton") {

      override def onSecureSubmit(target: AjaxRequestTarget, formModel: UserSummaryFormModel) {

        val validationResult = UserSummaryFormModelValidator.validate(formModel)
        if (validationResult.isValid) {
          saveChangesToUserProfile(formModel)
          copyFormDataIntoUserAndIncrementVersion(formModel)

          parent.switchToPreviewMode
          formModel.warningMessage = ""
        } else {
          formModel.warningMessage = "Formularz nie zostal wypelniony poprawnie"
        }
        target.add(parent)
      }
    })
  }

  def revertFormData(formModel: UserSummaryFormModel) {

    formModel.firstName = user.firstName
    formModel.lastName = user.lastName
    formModel.summary = user.summary

  }

  def copyFormDataIntoUserAndIncrementVersion(form: UserSummaryFormModel) {

    user.firstName = form.firstName
    user.lastName = form.lastName
    user.summary = form.summary

    user.incrementVersion
  }

  def saveChangesToUserProfile(formModel: UserSummaryFormModel) {
    userCommand.changeUserSummary(user.id, user.id, user.version, formModel.firstName, formModel.lastName, formModel.summary)
  }
}
