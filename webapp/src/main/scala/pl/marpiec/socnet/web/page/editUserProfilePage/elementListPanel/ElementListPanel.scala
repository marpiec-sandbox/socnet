package pl.marpiec.socnet.web.page.editUserProfilePage.elementListPanel

import org.apache.wicket.markup.html.panel.Panel
import collection.mutable.ListBuffer
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

class ElementListPanel[T](id: String, val user: User, val userProfile: UserProfile,
                          val elements: ListBuffer[T]) extends Panel(id) {

  //schema
  val elementList = addElementList
  var elementAdditionPanel = addElementAdditionPanel
  val showNewElementFormLink = addShowElementFormLink

  //methods

  def addElementList(): RepeatingView = {
    addAndReturn(new RepeatingView("repeating") {
      for (experience <- elements) {
        addElementToList(this, experience)
      }
    })
  }

  def addElementToList(experienceList: RepeatingView, element: T): MarkupContainer = {
    val item: AbstractItem = new AbstractItem(experienceList.newChildId());
    item.add(new ElementPanel[T]("content", user, userProfile, element))
    experienceList.add(item);
  }

  def addElementAdditionPanel(): ElementAdditionPanel[T] = {
    addAndReturn(new ElementAdditionPanel("jobExperienceAdditionPanel", this, user, userProfile))
  }

  def addShowElementFormLink(): AjaxLink[String] = {
    addAndReturn(new AjaxLink[String]("showNewExperienceFormLink") {
      setOutputMarkupId(true)
      setOutputMarkupPlaceholderTag(true)

      def onClick(target: AjaxRequestTarget) {
        showAddElementForm()
        target.add(elementAdditionPanel)
        target.add(this)
      }
    })
  }

  def hideAddElementForm() {
    elementAdditionPanel.setVisible(false)
    showNewElementFormLink.setVisible(true)
  }

  def showAddElementForm() {
    elementAdditionPanel.setVisible(true)
    showNewElementFormLink.setVisible(false)
  }

  def changeCurrentElementAdditionPanel(panel: ElementAdditionPanel[T]) {
    elementAdditionPanel = panel
  }

  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }

}
