package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperience

import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.Component
import pl.marpiec.socnet.web.page.editUserProfilePage.elementListPanel.ElementListPanel
import pl.marpiec.socnet.web.wicket.{SecureFormModel, SecureAjaxButton, SecureForm}

/**
 * @author Marcin Pieciukiewicz
 */

abstract class FormPanel[T, TM](id: String, mainListPanel: ElementListPanel[T, TM], newElement: Boolean, val element: T) extends Panel(id) {


  setOutputMarkupId(true)
  setOutputMarkupPlaceholderTag(true)

  setVisible(false)

  def onFormSubmit(target: AjaxRequestTarget, formModel: SecureFormModel)
  def onFormCanceled(target: AjaxRequestTarget, formModel: SecureFormModel)

  val form = addAndReturn(new SecureForm[TM]("form") {

    def initialize() {
      val formModel = mainListPanel.createModelFromElement(element)
      setModel(new CompoundPropertyModel[TM](formModel))
    }


    def buildSchema() {
      add(new Label("warningMessage"))
      mainListPanel.buildFormSchema(this)
      addCancelButton
      addSubmitButton
    }


    def addCancelButton {
      add(new SecureAjaxButton[TM]("cancelButton") {
        def onSecureSubmit(target: AjaxRequestTarget, formModel: TM) {
          onFormCanceled(target, formModel.asInstanceOf[SecureFormModel])
        }
      })
    }

    def addSubmitButton {
      add(new SecureAjaxButton[TM]("submitButton") {
        def onSecureSubmit(target: AjaxRequestTarget, formModel: TM) {
          onFormSubmit(target, formModel.asInstanceOf[SecureFormModel])
        }
      })
    }
  })

  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }
}
