package pl.marpiec.socnet.web.page.editUserProfilePage.personalSummaryPanel

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import pl.marpiec.socnet.model.{User, UserProfile}
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import org.apache.wicket.model.{PropertyModel, CompoundPropertyModel}
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import pl.marpiec.socnet.web.page.editUserProfilePage.model.PersonalSummaryFormModel
import org.apache.wicket.markup.html.form.{TextField, TextArea, Form}
import scala.Predef._

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class PersonalSummaryPanel(id: String, val user: User, val userProfile: UserProfile) extends Panel(id) {

  //dependencies
  val userProfileCommand: UserProfileCommand = Factory.userProfileCommand

  //initialization
  var edit = false
  setOutputMarkupId(true)

  //schema
  add(new WebMarkupContainer("personalSummaryPreview") {

    add(new Label("professionalTitle", new PropertyModel(userProfile, "professionalTitle")))
    add(new Label("city", new PropertyModel(userProfile, "city")))
    add(new Label("province", new PropertyModel(userProfile, "province")))
    add(new Label("wwwPage", new PropertyModel(userProfile, "wwwPage")))
    add(new Label("blogPage", new PropertyModel(userProfile, "blogPage")))
    add(new Label("summary", new PropertyModel(userProfile, "summary")))

    add(new AjaxFallbackLink("editButton") {
      def onClick(target: AjaxRequestTarget) {
        edit = true
        target.add(PersonalSummaryPanel.this)
      }
    })

    override def onConfigure() {
      setVisible(!edit)
    }
  })

  add(new SecureForm[PersonalSummaryFormModel]("personalSummaryForm") {

    val model = PersonalSummaryFormModel(userProfile)

    setModel(new CompoundPropertyModel[PersonalSummaryFormModel](model))

    add(new TextField[String]("professionalTitle"))
    add(new TextField[String]("city"))
    add(new TextField[String]("province"))
    add(new TextField[String]("wwwPage"))
    add(new TextField[String]("blogPage"))
    add(new TextArea[String]("summary"))

    add(new Label("userName", user.fullName))

    add(new SecureAjaxButton[PersonalSummaryFormModel]("cancelButton") {
      def onSecureSubmit(target: AjaxRequestTarget, formModel: PersonalSummaryFormModel) {
        revertFormData(formModel)
        edit = false
        target.add(PersonalSummaryPanel.this)
      }
    })

    add(new SecureAjaxButton[PersonalSummaryFormModel]("submitButton") {
      override def onSecureSubmit(target: AjaxRequestTarget, formModel: PersonalSummaryFormModel) {
        saveChangesToUserProfile(formModel)
        copyFormDataIntoUserProfileAndIncrementVersion(formModel)

        edit = false
        target.add(PersonalSummaryPanel.this)
      }
    })

    override def onConfigure() {
      setVisible(edit)
    }
  })

  //methods

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
