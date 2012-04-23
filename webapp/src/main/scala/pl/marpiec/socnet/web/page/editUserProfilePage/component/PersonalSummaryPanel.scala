package pl.marpiec.socnet.web.page.editUserProfilePage.component

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import pl.marpiec.socnet.model.{User, UserProfile}
import org.apache.wicket.model.{PropertyModel, CompoundPropertyModel}
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import pl.marpiec.socnet.service.userprofile.input.PersonalSummary
import org.apache.wicket.markup.html.form.{TextArea, TextField, Form}

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


  add(new Form[PersonalSummary]("personalSummaryForm") {

    val model = copyModelFromUserProfile(new PersonalSummary)

    setModel(new CompoundPropertyModel[PersonalSummary](model))

    add(new TextField[String]("professionalTitle"))
    add(new TextField[String]("city"))
    add(new TextField[String]("province"))
    add(new TextField[String]("wwwPage"))
    add(new TextField[String]("blogPage"))
    add(new TextArea[String]("summary"))

    add(new Label("userName", user.fullName))

    add(new AjaxButton("cancelButton") {
      def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
        val personalSummaryFormModel = form.getModel.asInstanceOf[CompoundPropertyModel[PersonalSummary]].getObject
        copyModelFromUserProfile(personalSummaryFormModel)
        edit = false
        target.add(PersonalSummaryPanel.this)
      }

      def onError(target: AjaxRequestTarget, form: Form[_]) {
        throw new IllegalStateException("Problem processing AJAX request")
      }
    })

    add(new AjaxButton("submitButton") {
      def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
        val personalSummaryFormModel = form.getModel.asInstanceOf[CompoundPropertyModel[PersonalSummary]].getObject
        saveChangesToUserProfile(personalSummaryFormModel)
        copyFormDataIntoUserProfile(personalSummaryFormModel)
        edit = false
        target.add(PersonalSummaryPanel.this)
      }

      def onError(target: AjaxRequestTarget, form: Form[_]) {
        throw new IllegalStateException("Problem processing AJAX request")
      }
    })

    override def onConfigure() {
      setVisible(edit)
    }
  })

  def copyModelFromUserProfile(model:PersonalSummary) = {
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
