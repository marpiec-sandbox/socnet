package pl.marpiec.socnet.web.component.wicket.form

import pl.marpiec.socnet.web.component.contacts.model.InviteUserFormModel
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.{TextArea, HiddenField, Form}
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureFormModel, SecureForm}
import org.apache.wicket.markup.html.panel.{PanelMarkupSourcingStrategy, IMarkupSourcingStrategy}

/**
 * @author Marcin Pieciukiewicz
 */

abstract class StandardAjaxSecureForm[M <: SecureFormModel](id:String) extends Form[M](id) {
  /**
   * {@inheritDoc}
   */

  initialize

  add(new Label("warningMessage"))
  add(new HiddenField[String]("sessionToken"))

  buildSchema

  add(new SecureAjaxButton[M]("cancelButton") {
    def onSecureSubmit(target: AjaxRequestTarget, formModel: M) {
      StandardAjaxSecureForm.this.onSecureCancel(target, formModel)
    }
  })

  add(new SecureAjaxButton[M]("submitButton") {
    def onSecureSubmit(target: AjaxRequestTarget, formModel: M) {
      StandardAjaxSecureForm.this.onSecureSubmit(target, formModel)
    }
  })

  override def onInitialize() {
    super.onInitialize()

    getModel.asInstanceOf[CompoundPropertyModel[SecureFormModel]].getObject.sessionToken =
      getSession.asInstanceOf[SocnetSession].sessionToken
  }

  def initialize
  def buildSchema

  def onSecureSubmit(target: AjaxRequestTarget, formModel: M)
  def onSecureCancel(target: AjaxRequestTarget, formModel: M)
}