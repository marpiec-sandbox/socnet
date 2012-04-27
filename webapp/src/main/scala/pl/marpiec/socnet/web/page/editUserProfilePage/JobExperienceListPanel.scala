package pl.marpiec.socnet.web.page.editUserProfilePage

import jobExperienceListPanel.{JobExperienceAdditionPanel, JobExperiencePanel}
import org.apache.wicket.markup.html.panel.Panel
import collection.mutable.ListBuffer
import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.model.{UserProfile, User}
import org.apache.wicket.{Component, MarkupContainer}


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperienceListPanel(id: String, val user: User, val userProfile: UserProfile,
                             val jobExperience: ListBuffer[JobExperience]) extends Panel(id) {


  //configure
  setOutputMarkupId(true)


  //schema
  val jobExperienceList = addJobExperienceList


  var jobExperienceAdditionPanel: JobExperienceAdditionPanel = addAndReturn(new JobExperienceAdditionPanel("jobExperienceAdditionPanel", this, user, userProfile));

  val showNewExperienceFormLink = addShowNewExperienceFormLink


  //methods

  def addShowNewExperienceFormLink(): AjaxLink[String] = {
    addAndReturn(new AjaxLink[String]("showNewExperienceFormLink") {
      setOutputMarkupId(true)
      setOutputMarkupPlaceholderTag(true)

      def onClick(target: AjaxRequestTarget) {
        showAddExperienceForm()
        target.add(jobExperienceAdditionPanel)
        target.add(this)
      }
    })
  }

  def hideAddExperienceForm() {
    jobExperienceAdditionPanel.setVisible(false)
    showNewExperienceFormLink.setVisible(true)
  }

  def showAddExperienceForm() {
    jobExperienceAdditionPanel.setVisible(true)
    showNewExperienceFormLink.setVisible(false)
  }

  def setNewJobExperienceAdditionPanel(panel: JobExperienceAdditionPanel) {
    jobExperienceAdditionPanel = panel
  }

  def addJobExperienceList(): RepeatingView = {
    addAndReturn(new RepeatingView("repeating") {
      for (experience <- jobExperience) {
        addExperience(this, experience)
      }
    })
  }

  def addExperience(experienceList: RepeatingView, experience: JobExperience): MarkupContainer = {
    val item: AbstractItem = new AbstractItem(experienceList.newChildId());
    item.add(new JobExperiencePanel("content", user, userProfile, experience))
    experienceList.add(item);
  }


  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }


}
