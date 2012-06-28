package pl.marpiec.socnet.web.page.registration.registerPage

import org.apache.wicket.model.{CompoundPropertyModel, Model}
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.page.HomePage
import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.panel.{Panel, FeedbackPanel}
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.registration.ConfirmRegistrationPage
import pl.marpiec.socnet.service.user.UserCommand
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.exception.EntryAlreadyExistsException

class RegisterForm(id: String) extends Panel(id) {

  @SpringBean private var userCommand: UserCommand = _

  add(new FeedbackPanel("feedback"))
  add(new StatelessForm[RegisterFormModel]("form") {


    private val warningMessage: Model[String] = new Model[String]("");

    setModel(new CompoundPropertyModel[RegisterFormModel](new RegisterFormModel))

    add(new Label("warningMessage", warningMessage))
    add(new TextField[String]("firstName"))
    add(new TextField[String]("lastName"))
    add(new TextField[String]("email"))
    add(new PasswordTextField("password"))
    add(new PasswordTextField("repeatPassword"))

    add(new BookmarkablePageLink("cancelLink", classOf[HomePage]))


    override def onSubmit() {
      try {

        val model: RegisterFormModel = getDefaultModelObject.asInstanceOf[RegisterFormModel]

        val validationResult = RegisterFormValidator.validate(model)

        if (validationResult.isValid) {

          try {
            userCommand.createRegisterUserTrigger(model.firstName, model.lastName, model.email, model.password)
            setResponsePage(classOf[ConfirmRegistrationPage])
          } catch {
            case ex: EntryAlreadyExistsException => {
              warningMessage.setObject("Uzytkownik o podasnym adresie email zostal juz zarejestrowany")
            }
          }
        } else {
          warningMessage.setObject("Formularz nie zostal wypelniony poprawnie")
        }

      } catch {
        case e: EntryAlreadyExistsException => warningMessage.setObject("User already registered")
      }
    }
  })


}
