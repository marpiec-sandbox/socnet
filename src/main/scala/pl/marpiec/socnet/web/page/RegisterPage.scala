package pl.marpiec.socnet.web.page

import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.form.{Button, TextField, Form}
import pl.marpiec.di.Factory
import pl.marpiec.util.Strings
import pl.marpiec.socnet.service.user.exception.UserAlreadyRegisteredException
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.{Model, PropertyModel, CompoundPropertyModel}

class RegisterFormModel {
  var username:String = _
  var email:String = _
  var password:String = _
  var repeatPassword:String = _
}


class RegisterForm(name:String) extends Form[RegisterFormModel](name) {
  
  val userCommand = Factory.userCommand

  val warningMessage:Model[String] = new Model[String]("");

  setModel(new CompoundPropertyModel[RegisterFormModel](new RegisterFormModel))

  add(new Label("warningMessage", warningMessage))
  add(new TextField[String]("username").setRequired(true))
  add(new TextField[String]("email").setRequired(true))
  add(new TextField[String]("password").setRequired(true))
  add(new TextField[String]("repeatPassword").setRequired(true))
  add(new Button("saveButton"))
  add(new Button("resetButton"))

  override def onSubmit() {
    try {

      val model: RegisterFormModel = getDefaultModelObject.asInstanceOf[RegisterFormModel]

      if(Strings.equal(model.password, model.repeatPassword)) {
        userCommand.registerUser(model.username, model.email, model.password)
        setResponsePage(classOf[HomePage])
      } else {
        warningMessage.setObject("Passwords does not match")
      }
    } catch {
      case e:UserAlreadyRegisteredException => warningMessage.setObject("User already registered")
    }
  }
}

class RegisterPage(parameters: PageParameters) extends WebPage {

  add(new RegisterForm("registerForm"))
  

}
