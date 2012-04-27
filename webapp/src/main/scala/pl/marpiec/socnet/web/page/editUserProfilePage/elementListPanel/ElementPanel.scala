package pl.marpiec.socnet.web.page.editUserProfilePage.elementListPanel

import elementPanel.{PreviewPanel, FormPanel}
import org.apache.wicket.markup.html.panel.Panel

import pl.marpiec.socnet.model.{User, UserProfile}
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.web.wicket.SecureFormModel
import socnet.model.userprofile.Identifiable

/**
 * ...
 * @author Marcin Pieciukiewicz
 */


class ElementPanel[T <: Identifiable, TM <: SecureFormModel](id: String, mainListPanel:ElementListPanel[T, TM],
                                          val user: User, val userProfile: UserProfile, val element: T)
  extends Panel(id) {

  //dependencies
  val userProfileCommand = Factory.userProfileCommand

  //configure
  setOutputMarkupId(true)

  //schema
  val previewPanel = addPreviewPanel
  val editForm = addEditForm


  //methods

  def addPreviewPanel():Panel = {
    addAndReturn(new PreviewPanel("elementPreview", mainListPanel, this, element, userProfile))
  }

  def addEditForm:FormPanel[T, TM] = {
    addAndReturn(new FormPanel[T, TM]("elementForm", mainListPanel, false, element) {
      def onFormSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) = {

        formModel.warningMessage = ""

        val validationResult = mainListPanel.validate(formModel.asInstanceOf[TM])

        if (validationResult.isValid) {
          saveChangesToElement(formModel.asInstanceOf[TM])
          copyDataIntoElementAndIncrementProfileVersion(formModel.asInstanceOf[TM])
          switchToPreviewMode
        } else {
          formModel.warningMessage = "Formularz nie zostal wypelniony poprawnie"
        }

        target.add(ElementPanel.this)
      }

      def onFormCanceled(target: AjaxRequestTarget, formModel: SecureFormModel) = {
        revertFormData(formModel.asInstanceOf[TM])
        switchToPreviewMode
        target.add(ElementPanel.this)
      }

    })
  }


  def revertFormData(formModel: TM) {
    mainListPanel.copyElementToModel(formModel, element.asInstanceOf[T])
  }

  def saveChangesToElement(formModel: TM) {
    mainListPanel.saveChangesToElement(formModel)

  }


  def copyDataIntoElementAndIncrementProfileVersion(formModel: TM) {
    mainListPanel.copyModelToElement(element, formModel)
    userProfile.incrementVersion
  }

  def switchToPreviewMode {
    editForm.setVisible(false)
    previewPanel.setVisible(true)
  }

  def switchToEditMode {
    editForm.setVisible(true)
    previewPanel.setVisible(false)
  }

  def hideRemovedElement {
    this.setVisible(false)
  }


  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }


}
