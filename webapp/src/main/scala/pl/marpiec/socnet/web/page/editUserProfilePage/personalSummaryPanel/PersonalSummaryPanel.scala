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
import pl.marpiec.socnet.service.userprofile.input.PersonalSummary
import org.apache.wicket.markup.html.form.{TextArea, TextField, Form}
import org.apache.wicket.model.{PropertyModel, CompoundPropertyModel}
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import pl.marpiec.socnet.web.page.editUserProfilePage.model.PersonalSummaryFormModel

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class PersonalSummaryPanel(id: String, val user: User, val userProfile: UserProfile) extends Panel(id) {

  val userProfileCommand: UserProfileCommand = Factory.userProfileCommand

  var edit = false

  setOutputMarkupId(true)

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

    val model = copyModelFromUserProfile(new PersonalSummaryFormModel)

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
        val personalSummaryFormModel = form.getModel.asInstanceOf[CompoundPropertyModel[PersonalSummaryFormModel]].getObject
        copyModelFromUserProfile(personalSummaryFormModel)
        edit = false
        target.add(PersonalSummaryPanel.this)
      }

      def onError(target: AjaxRequestTarget, form: Form[_]) {
        throw new IllegalStateException("Problem processing AJAX request")
      }
    })

    add(new SecureAjaxButton("submitButton") {
      override def onSecureSubmit(target: AjaxRequestTarget, form: Form[_]) {
        val personalSummaryFormModel = form.getModel.asInstanceOf[CompoundPropertyModel[PersonalSummaryFormModel]].getObject

        saveChangesToUserProfile(personalSummaryFormModel)
        copyFormDataIntoUserProfile(personalSummaryFormModel)
        edit = false
        target.add(PersonalSummaryPanel.this)
      }
    })

    override def onConfigure() {
      setVisible(edit)
    }
  })

  def copyModelFromUserProfile(model: PersonalSummaryFormModel): PersonalSummaryFormModel = {
    model.blogPage = userProfile.blogPage
    model.wwwPage = userProfile.wwwPage
    model.professionalTitle = userProfile.professionalTitle
    model.city = userProfile.city
    model.province = userProfile.province
    model.summary = userProfile.summary
    model
  }

  def copyFormDataIntoUserProfile(form: PersonalSummary) {
    userProfile.professionalTitle = form.professionalTitle
    userProfile.city = form.city
    userProfile.province = form.province
    userProfile.wwwPage = form.wwwPage
    userProfile.blogPage = form.blogPage
    userProfile.summary = form.summary
    userProfile.version = userProfile.version + 1
  }

  def saveChangesToUserProfile(personalSummary: PersonalSummary) {
    userProfileCommand.updatePersonalSummary(user.id, userProfile.id, userProfile.version, personalSummary)
  }
}