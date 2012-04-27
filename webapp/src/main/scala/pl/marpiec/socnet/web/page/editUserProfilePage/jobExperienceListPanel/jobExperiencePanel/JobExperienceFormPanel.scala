package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperienceListPanel.jobExperiencePanel

import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.basic.Label
import socnet.constant.Month
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureForm}
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.page.editUserProfilePage.model.JobExperienceFormModel
import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.html.form._
import scala.collection.JavaConversions._
import org.apache.wicket.Component

/**
 * @author Marcin Pieciukiewicz
 */

abstract class JobExperienceFormPanel(id: String, newExperience: Boolean, val jobExperience: JobExperience) extends Panel(id) {


  setOutputMarkupId(true)
  setOutputMarkupPlaceholderTag(true)

  setVisible(false)

  def onFormSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel)

  def onFormCanceled(target: AjaxRequestTarget, formModel: JobExperienceFormModel)


  val form = addAndReturn(new SecureForm[JobExperienceFormModel]("form") {

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
      add(new TextField[Int]("fromYear"))
      add(new TextField[Int]("toYear"))

      add(new DropDownChoice[Month]("fromMonth", Month.values, new ChoiceRenderer[Month]("translation")))
      add(new DropDownChoice[Month]("toMonth", Month.values, new ChoiceRenderer[Month]("translation")))

      addCancelButton
      addSubmitButton
    }


    def addCancelButton {
      add(new SecureAjaxButton[JobExperienceFormModel]("cancelButton") {
        def onSecureSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
          onFormCanceled(target, formModel)
        }
      })
    }

    def addSubmitButton {
      add(new SecureAjaxButton[JobExperienceFormModel]("submitButton") {
        def onSecureSubmit(target: AjaxRequestTarget, formModel: JobExperienceFormModel) {
          onFormSubmit(target, formModel)
        }
      })
    }
  })

  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }
}
