package pl.marpiec.socnet.web.page.editUserProfilePage.elementListPanel

import org.apache.wicket.markup.html.panel.Panel
import collection.mutable.ListBuffer
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.model.{UserProfile, User}
import org.apache.wicket.{Component, MarkupContainer}
import org.apache.wicket.markup.html.form.Form
import pl.marpiec.util.ValidationResult
import pl.marpiec.socnet.web.wicket.SecureFormModel


/**
 * @author Marcin Pieciukiewicz
 */

abstract class ElementListPanel[T, TM](id: String, val user: User, val userProfile: UserProfile,
                                       val elements: ListBuffer[T]) extends Panel(id) {



  //schema
  val elementList = addElementList
  var elementAdditionPanel = addElementAdditionPanel
  val showNewElementFormLink = addShowElementFormLink

  //abstract methods
  def removeElement(element: T)
  def createNewElement: T
  def buildFormSchema(form: Form)
  def buildPreviewSchema(panel: Panel, element: T)
  def validate(form: TM): ValidationResult
  def createModelFromElement(element: T): TM
  def copyElementToModel(model: TM, element: T)
  def copyModelToElement(element: T, model: TM)

  //methods
  def addElementList(): RepeatingView = {
    addAndReturn(new RepeatingView("repeating") {
      for (element <- elements) {
        addElementToList(this, element)
      }
    })
  }

  def addElementToList(elementList: RepeatingView, element: T): MarkupContainer = {
    val item: AbstractItem = new AbstractItem(elementList.newChildId());
    item.add(new ElementPanel[T, TM]("content", this, user, userProfile, element))
    elementList.add(item);
  }

  def addElementAdditionPanel(): ElementAdditionPanel[T, TM] = {
    addAndReturn(new ElementAdditionPanel("elementAdditionPanel", this, user, userProfile))
  }

  def addShowElementFormLink(): AjaxLink[String] = {
    addAndReturn(new AjaxLink[String]("showNewElementFormLink") {
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

  def changeCurrentElementAdditionPanel(panel: ElementAdditionPanel[T, TM]) {
    elementAdditionPanel = panel
  }

  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }

}
