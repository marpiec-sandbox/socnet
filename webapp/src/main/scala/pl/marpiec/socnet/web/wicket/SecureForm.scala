package pl.marpiec.socnet.web.wicket

import org.apache.wicket.markup.html.form.{HiddenField, Form}
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.web.page.editUserProfilePage.component.PersonalSummaryFormModel


/**
 * @author Marcin Pieciukiewicz
 */

class SecureForm[M](id:String) extends Form[M](id) {


  add(new HiddenField[String]("sessionToken"))

  override def onInitialize() {
    super.onInitialize()

    getModel.asInstanceOf[CompoundPropertyModel[SecureFormModel]].getObject.sessionToken =
      getSession.asInstanceOf[SocnetSession].sessionToken
  }
}
