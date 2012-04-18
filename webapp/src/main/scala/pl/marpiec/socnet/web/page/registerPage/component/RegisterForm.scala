package pl.marpiec.socnet.web.page.registerPage.component

import pl.marpiec.socnet.di.Factory
import org.apache.wicket.model.{CompoundPropertyModel, Model}
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.page.homePage.HomePage
import pl.marpiec.util.Strings
import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel.{Panel, FeedbackPanel}
import pl.marpiec.socnet.database.exception.EntryAlreadyExistsException

class RegisterForm(id: String) extends Panel(id) {

  add(new FeedbackPanel("feedback"))
  add(new StatelessForm[RegisterFormModel]("form") {

    private val userCommand = Factory.userCommand

    private val warningMessage: Model[String] = new Model[String]("");

    setModel(new CompoundPropertyModel[RegisterFormModel](new RegisterFormModel))

    add(new Label("warningMessage", warningMessage))
    add(new TextField[String]("username"))
    add(new TextField[String]("email"))
    add(new PasswordTextField("password"))
    add(new PasswordTextField("repeatPassword"))
    add(new Button("saveButton"))

    add(new Button("cancelButton") {

      setDefaultFormProcessing(false)

      override def onSubmit() {
        setResponsePage(classOf[HomePage])
      }
    })


    override def onSubmit() {
      try {

        val model: RegisterFormModel = getDefaultModelObject.asInstanceOf[RegisterFormModel]

        val validationResult = RegisterFormValidator.validate(model)

        if (validationResult.isValid) {
          userCommand.registerUser(model.username, model.email, model.password)
          setResponsePage(classOf[HomePage])
        } else {
          warningMessage.setObject(validationResult.errors.toString())
        }

      } catch {
        case e: EntryAlreadyExistsException => warningMessage.setObject("User already registered")
      }
    }
  })


}
