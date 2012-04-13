package pl.marpiec.socnet.web.page.userProfile.component

import pl.marpiec.socnet.model.User
import org.apache.wicket.markup.html.panel.Panel
import collection.mutable.ListBuffer
import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.service.userprofile.input.JobExperienceParam
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.{TextArea, TextField, Form}
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.MarkupContainer
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperienceListPanel(id: String, val user: User, val jobExperience: ListBuffer[JobExperience])
  extends Panel(id) {

  setOutputMarkupId(true)


  val jobExperienceList: RepeatingView = new RepeatingView("repeating") {
  for (experience <- jobExperience) {
      addExperienceToJobExperienceList(this, experience)
    }
  }
  
  def addExperienceToJobExperienceList(repeating: RepeatingView, experience: JobExperience): MarkupContainer = {
    val item: AbstractItem = new AbstractItem(repeating.newChildId());
    item.add(new JobExperiencePanel("content", user, experience))
    repeating.add(item);
  }



  val newJobExperienceForm: Form[JobExperienceParam] = new Form[JobExperienceParam]("newJobExperienceForm") {
    setOutputMarkupPlaceholderTag(true)
    setVisible(false)

    setModel(new CompoundPropertyModel[JobExperienceParam](new JobExperienceParam))

    add(new TextField[String]("companyName"))
    add(new TextField[String]("position"))
    add(new TextArea[String]("description"))


    add(new AjaxButton("cancelButton") {
      def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
        val model = form.getModel.asInstanceOf[CompoundPropertyModel[JobExperienceParam]].getObject
        cleanModel(model)
        newJobExperienceForm.setVisible(false)
        showNewExperienceFormLink.setVisible(true)
        target.add(JobExperienceListPanel.this)

      }

      def onError(target: AjaxRequestTarget, form: Form[_]) {
        throw new IllegalStateException("Problem processing AJAX request")
      }
    })

    add(new AjaxButton("submitButton") {
      def onSubmit(target: AjaxRequestTarget, form: Form[_]) {

        val model = form.getModel.asInstanceOf[CompoundPropertyModel[JobExperienceParam]].getObject
        
        val experience = new JobExperience
        experience.companyName = model.companyName
        experience.description = model.description
        experience.position = model.position
        experience.id = UID.generate

        addExperienceToJobExperienceList(jobExperienceList, experience)
        
        cleanModel(model)
        
        newJobExperienceForm.setVisible(false)
        showNewExperienceFormLink.setVisible(true)
        target.add(JobExperienceListPanel.this)
      }

      def onError(target: AjaxRequestTarget, form: Form[_]) {
        throw new IllegalStateException("Problem processing AJAX request")
      }
    })
  }

  def cleanModel(model:JobExperienceParam) {
    model.companyName = ""
    model.description = ""
    model.position = ""
  }


  val showNewExperienceFormLink: AjaxLink[String] = new AjaxLink[String]("showNewExperienceFormLink") {
    setOutputMarkupId(true)

    def onClick(target: AjaxRequestTarget) {
      newJobExperienceForm.setVisible(true)
      this.setVisible(false)
      target.add(newJobExperienceForm)
      target.add(this)
    }
  }

  add(jobExperienceList)
  add(newJobExperienceForm)
  add(showNewExperienceFormLink)

}
