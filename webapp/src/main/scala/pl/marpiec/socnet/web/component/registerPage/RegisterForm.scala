package pl.marpiec.socnet.web.component.registerPage

import pl.marpiec.socnet.di.Factory
import org.apache.wicket.model.{CompoundPropertyModel, Model}
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.page.HomePage
import pl.marpiec.util.Strings
import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel.{Panel, FeedbackPanel}
import pl.marpiec.socnet.database.exception.EntryAlreadyExistsException

class RegisterForm(id: String) extends Panel(id) {

  add(new FeedbackPanel("feedback"))
  add(new Form[RegisterFormModel]("form") {

    private val userCommand = Factory.userCommand

    private val warningMessage: Model[String] = new Model[String]("");

    setModel(new CompoundPropertyModel[RegisterFormModel](new RegisterFormModel))

    add(new Label("warningMessage", warningMessage))
    add(new TextField[String]("username").setRequired(true))
    add(new TextField[String]("email").setRequired(true))
    add(new TextField[String]("password").setRequired(true))
    add(new TextField[String]("repeatPassword").setRequired(true))
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

        if (Strings.equal(model.password, model.repeatPassword)) {
          userCommand.registerUser(model.username, model.email, model.password)
          setResponsePage(classOf[HomePage])
        } else {
          warningMessage.setObject("Passwords does not match")
        }
      } catch {
        case e: EntryAlreadyExistsException => warningMessage.setObject("User already registered")
      }
    }
  })


}