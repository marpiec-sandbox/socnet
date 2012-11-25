package pl.marpiec.socnet.web.component.wicket.form

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.form.{HiddenField, Form}
import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureFormModel}
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class OneLinkAjaxForm(id: String, buttonLabel: String, onClick: (AjaxRequestTarget) => Unit) extends Panel(id) {

  add(new Form[SecureFormModel]("form") {

    setModel(new CompoundPropertyModel[SecureFormModel](new SecureFormModel))

    add(new HiddenField[String]("sessionToken"))

    override def onInitialize() {
      super.onInitialize()

      getModel.asInstanceOf[CompoundPropertyModel[SecureFormModel]].getObject.sessionToken =
        getSession.asInstanceOf[SocnetSession].sessionToken
    }

    add(new SecureAjaxButton[SecureFormModel]("button") {
      add(new Label("label", buttonLabel))

      def onSecureSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {
        onClick(target)
      }
    })


  })

}

