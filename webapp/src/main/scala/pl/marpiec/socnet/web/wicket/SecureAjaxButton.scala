package pl.marpiec.socnet.web.wicket

import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.web.page.editUserProfilePage.component.PersonalSummaryFormModel
import pl.marpiec.util.Strings
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.Form

/**
 * @author Marcin Pieciukiewicz
 */

abstract class SecureAjaxButton(id:String) extends AjaxButton(id) {


  final override def onSubmit(target: AjaxRequestTarget, form: Form[_]) {

    val formModel = getForm.getModel.asInstanceOf[CompoundPropertyModel[SecureFormModel]].getObject

    if (Strings.isNotEqual(formModel.sessionToken, getSession.asInstanceOf[SocnetSession].sessionToken)) {
      throw new IllegalStateException("Incorrect session authentication data!")
    }

    onSecureSubmit(target, form)
  }

  def onSecureSubmit(target: AjaxRequestTarget, form: Form[_])
}
