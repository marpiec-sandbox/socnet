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

    add(new AjaxButton("cancelButton") {
      def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
        val formModel = form.getModel.asInstanceOf[CompoundPropertyModel[PersonalSummaryFormModel]].getObject
        PersonalSummaryFormModel.copy(formModel, userProfile)
        edit = false
        target.add(PersonalSummaryPanel.this)
      }

      def onError(target: AjaxRequestTarget, form: Form[_]) {
        throw new IllegalStateException("Problem processing AJAX request")
      }
    })

    add(new SecureAjaxButton("submitButton") {
      override def onSecureSubmit(target: AjaxRequestTarget, form: Form[_]) {
        val formModel = form.getModel.asInstanceOf[CompoundPropertyModel[PersonalSummaryFormModel]].getObject

        saveChangesToUserProfile(formModel)
        copyFormDataIntoUserProfile(formModel)

        userProfile.incrementVersion
        edit = false
        target.add(PersonalSummaryPanel.this)
      }
    })

    override def onConfigure() {
      setVisible(edit)
    }
  })

  def copyFormDataIntoUserProfile(form: PersonalSummaryFormModel) {
    PersonalSummaryFormModel.copy(userProfile, form)
    userProfile.version = userProfile.version + 1
  }

  def saveChangesToUserProfile(personalSummary: PersonalSummaryFormModel) {
    userProfileCommand.updatePersonalSummary(user.id, userProfile.id, userProfile.version, personalSummary.createPersonalSummary)
  }
}
