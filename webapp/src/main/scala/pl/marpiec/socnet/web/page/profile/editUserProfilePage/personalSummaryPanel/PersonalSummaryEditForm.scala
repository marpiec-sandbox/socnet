package pl.marpiec.socnet.web.page.profile.editUserProfilePage.personalSummaryPanel

import scala.collection.JavaConversions._
import pl.marpiec.socnet.model.{User, UserProfile}
import pl.marpiec.socnet.web.page.profile.editUserProfilePage.PersonalSummaryPanel
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.constant.Province
import org.apache.wicket.markup.html.form._
import pl.marpiec.socnet.web.page.profile.editUserProfilePage.model.{PersonalSummaryFormModelValidator, PersonalSummaryFormModel}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.spring.injection.annot.SpringBean


class PersonalSummaryEditForm(id: String, userProfile: UserProfile, user: User, parent: PersonalSummaryPanel) extends SecureForm[PersonalSummaryFormModel](id) {

  @SpringBean private var userProfileCommand: UserProfileCommand = _

  def initialize {
    val model = PersonalSummaryFormModel(userProfile)
    setModel(new CompoundPropertyModel[PersonalSummaryFormModel](model))
    setVisible(false)
  }

  def buildSchema {

    add(new Label("warningMessage"))
    add(new TextField[String]("city"))
    add(new DropDownChoice[Province]("province", Province.values, new ChoiceRenderer[Province]("translation")))

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

      override def onSecureSubmit(target: AjaxRequestTarget, formModel: PersonalSummaryFormModel) {


        val validationResult = PersonalSummaryFormModelValidator.validate(formModel)
        if (validationResult.isValid) {
          saveChangesToUserProfile(formModel)
          copyFormDataIntoUserProfileAndIncrementVersion(formModel)

          parent.switchToPreviewMode
          formModel.warningMessage = ""
        } else {
          formModel.warningMessage = "Formularz nie zostal wypelniony poprawnie"
        }
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
