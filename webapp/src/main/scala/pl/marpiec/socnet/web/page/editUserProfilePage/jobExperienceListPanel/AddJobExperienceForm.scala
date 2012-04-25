package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperienceListPanel

import pl.marpiec.socnet.web.page.editUserProfilePage.model.JobExperienceFormModel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.form.{TextArea, TextField}
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.page.editUserProfilePage.JobExperienceListPanel

/**
 * @author Marcin Pieciukiewicz
 */

class AddJobExperienceForm(id: String, parent: JobExperienceListPanel) extends SecureForm[JobExperienceFormModel](id) {

  def initialize {
    setOutputMarkupPlaceholderTag(true)
    setVisible(false)

    setModel(new CompoundPropertyModel[JobExperienceFormModel](new JobExperienceFormModel))
  }

  def buildSchema {
    add(new TextField[String]("companyName"))
    add(new TextField[String]("position"))
    add(new TextArea[String]("description"))

    addCancelButton
    addSubmitButton
  }


  def addCancelButton {
    add(new SecureAjaxButton[JobExperienceFormModel]("cancelButton") {
      def onSecureSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
        formModel.clear()
        parent.hideAddExperienceForm()
        target.add(parent)
      }
    })
  }

  def addSubmitButton {
    add(new SecureAjaxButton[JobExperienceFormModel]("submitButton") {
      def onSecureSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {

        parent.saveNewExperience(formModel)

        formModel.clear()

        parent.hideAddExperienceForm()
        target.add(parent)
      }
    })
  }


}