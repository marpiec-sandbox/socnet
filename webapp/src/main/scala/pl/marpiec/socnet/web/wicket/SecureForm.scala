package pl.marpiec.socnet.web.wicket

import org.apache.wicket.markup.html.form.{HiddenField, Form}
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.model.CompoundPropertyModel


/**
 * @author Marcin Pieciukiewicz
 */

abstract class SecureForm[M <: SecureFormModel](id:String) extends Form[M](id) {

  initialize
  buildSchema

  add(new HiddenField[String]("sessionToken"))



  def initialize
  def buildSchema

  override def onInitialize() {
    super.onInitialize()

    getModel.asInstanceOf[CompoundPropertyModel[SecureFormModel]].getObject.sessionToken =
      getSession.asInstanceOf[SocnetSession].sessionToken
  }
}
