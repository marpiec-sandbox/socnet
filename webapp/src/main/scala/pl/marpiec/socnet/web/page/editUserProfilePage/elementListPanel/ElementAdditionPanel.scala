package pl.marpiec.socnet.web.page.editUserProfilePage.elementListPanel

import elementPanel.FormPanel
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.util.UID
import org.apache.wicket.Component
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.{UserProfile, User}
import org.apache.wicket.markup.html.panel.{EmptyPanel, Panel}
import pl.marpiec.socnet.web.wicket.SecureFormModel
import socnet.model.userprofile.Identifiable

/**
 * @author Marcin Pieciukiewicz
 */

class ElementAdditionPanel[T <: Identifiable, TM <: SecureFormModel](id: String, mainListPanel: ElementListPanel[T, TM],
                                  val user: User, val userProfile: UserProfile) extends Panel(id) {

  //dependencies
  val userProfileCommand = Factory.userProfileCommand
  val uidGenerator = Factory.uidGenerator

  setOutputMarkupId(true)
  setOutputMarkupPlaceholderTag(true)
  setVisible(false)

  add(new EmptyPanel("elementPanel"))
  add(new EmptyPanel("elementAdditionPanel"))

  add(new FormPanel[T, TM]("addElementForm", mainListPanel, true, mainListPanel.createNewElement) {

    setVisible(true)

    def onFormSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {

      formModel.warningMessage = ""
      val validationResult = mainListPanel.validate(formModel.asInstanceOf[TM])

      if (validationResult.isValid) {
        val element = saveNewElement(formModel)
        showAddedElement(element)
        changeCurrentElementAdditionPanel

        formModel.clear()
        this.setVisible(false)
        mainListPanel.hideAddElementForm
        target.add(mainListPanel.showNewElementFormLink)

      } else {
        formModel.warningMessage = "Formularz nie zostal wypelniony poprawnie"
      }

      target.add(ElementAdditionPanel.this)

    }

    def onFormCanceled(target: AjaxRequestTarget, formModel: SecureFormModel) {
      formModel.clear()
      mainListPanel.hideAddElementForm
      target.add(ElementAdditionPanel.this)
      target.add(mainListPanel.showNewElementFormLink)
    }
  })

  def changeCurrentElementAdditionPanel {
    mainListPanel.changeCurrentElementAdditionPanel(addAndReturn(
        new ElementAdditionPanel[T, TM]("elementAdditionPanel", mainListPanel, user, userProfile)))
  }

  def showAddedElement(element: T) {
    addOrReplace(new ElementPanel[T, TM]("elementPanel", mainListPanel, user, userProfile, element))
  }

  def saveNewElement(formModel: SecureFormModel): T = {
    val newElementId = uidGenerator.nextUid

    saveNewElementAndIncrementProfileVersion(formModel, newElementId)

    val element = mainListPanel.createNewElement
    mainListPanel.copyModelToElement(element, formModel.asInstanceOf[TM])
    element.id = newElementId
    element
  }


  def saveNewElementAndIncrementProfileVersion(formModel: SecureFormModel, newElementId: UID) {
    mainListPanel.saveNewElement(formModel.asInstanceOf[TM], newElementId)
    userProfile.incrementVersion
  }


  private def addAndReturn[E <: Component](child: E): E = {
    addOrReplace(child)
    child
  }


}
