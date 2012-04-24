package pl.marpiec.socnet.web.wicket

import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.Form
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

abstract class SecureAjaxButton(id:String) extends AjaxButton(id) {


  final override def onSubmit(target: AjaxRequestTarget, form: Form[_]) {

    val formModel = getForm.getModel.asInstanceOf[CompoundPropertyModel[SecureFormModel]].getObject

    if (!StringUtils.equals(formModel.sessionToken, getSession.asInstanceOf[SocnetSession].sessionToken)) {
      throw new IllegalStateException("Incorrect session authentication data!")
    }

    onSecureSubmit(target, form)
  }

  def onSecureSubmit(target: AjaxRequestTarget, form: Form[_])

  def onError(target: AjaxRequestTarget, form: Form[_]) {
    throw new IllegalStateException("Problem processing AJAX request")
  }
}
