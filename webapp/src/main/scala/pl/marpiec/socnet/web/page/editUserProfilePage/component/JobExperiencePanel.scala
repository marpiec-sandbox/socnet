package pl.marpiec.socnet.web.page.editUserProfilePage.component

import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.{CompoundPropertyModel, PropertyModel}
import pl.marpiec.socnet.service.userprofile.input.JobExperienceParam
import org.apache.wicket.markup.html.form.{TextArea, TextField, Form}
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.{UserProfile, User}

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


  add(new Form[JobExperienceParam]("experienceForm") {

    val jobExperienceModel = new JobExperienceParam
    jobExperienceModel.companyName = jobExperience.companyName
    jobExperienceModel.position = jobExperience.position
    jobExperienceModel.description = jobExperience.description
    jobExperienceModel.id = jobExperience.id

    setModel(new CompoundPropertyModel[JobExperienceParam](jobExperienceModel))

    add(new TextField[String]("companyName"))
    add(new TextField[String]("position"))
    add(new TextArea[String]("description"))


    add(new AjaxButton("cancelButton") {
      def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
        val model = form.getModel.asInstanceOf[CompoundPropertyModel[JobExperienceParam]].getObject
        model.companyName = jobExperience.companyName
        model.position = jobExperience.position
        model.description = jobExperience.description
        model.id = null
        edit = false
        target.add(JobExperiencePanel.this)
      }

      def onError(target: AjaxRequestTarget, form: Form[_]) {
        throw new IllegalStateException("Problem processing AJAX request")
      }
    })

    add(new AjaxButton("submitButton") {
      def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
        val model = form.getModel.asInstanceOf[CompoundPropertyModel[JobExperienceParam]].getObject
        jobExperience.companyName = model.companyName
        jobExperience.position = model.position
        jobExperience.description = model.description
        jobExperience.id = model.id

        userProfileCommand.updateJobExperience(user.id, userProfile.id, userProfile.version, model)
        userProfile.incrementVersion

        edit = false
        target.add(JobExperiencePanel.this)
      }

      def onError(target: AjaxRequestTarget, form: Form[_]) {
        throw new IllegalStateException("Problem processing AJAX request")
      }
    })

    override def onConfigure() {
      setVisible(edit)
    }
  });

}
