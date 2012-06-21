package pl.marpiec.socnet.service.user

import event.{ChangeSummaryEvent, ChangeEmailEvent, RegisterUserEvent, ChangeForgottenPasswordEvent}
import pl.marpiec.cqrs._
import exception.IncorrectTriggerException
import pl.marpiec.mailsender.MailSender
import pl.marpiec.socnet.mailtemplate.TemplateRepository
import pl.marpiec.util.{PasswordUtil, UID}
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.service.exception.EmailDoesNotExistsException
import scala.Predef._
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.UserDatabase
import pl.marpiec.socnet.readdatabase.exception.EntryAlreadyExistsException

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Service("userCommand")
class UserCommandImpl @Autowired()(val eventStore: EventStore, val dataStore: DataStore,
                                   val triggeredEvents: TriggeredEvents, val userDatabase: UserDatabase,
                                   val uidGenerator: UidGenerator, val mailSender: MailSender,
                                   val templateRepository: TemplateRepository) extends UserCommand {


  override def triggerUserRegistration(trigger: String): UID = {

    val eventOption = triggeredEvents.getEventForTrigger(trigger)

    if (eventOption.isEmpty || !eventOption.get.isInstanceOf[RegisterUserEvent]) {
      throw new IncorrectTriggerException
    }

    val registerUser = eventOption.get.asInstanceOf[RegisterUserEvent]

    if (userDatabase.getUserByEmail(registerUser.email).isDefined) {
      throw new EntryAlreadyExistsException
    }

    //TODO pomyslec o synchronizacji

    val id = uidGenerator.nextUid
    eventStore.addEventForNewAggregate(id, new EventRow(id, id, 0, registerUser))

    triggeredEvents.markEventAsExecuted(trigger)

    id
  }

  def createRegisterUserTrigger(firstName: String, lastName: String, email: String, password: String) = {
    if (userDatabase.getUserByEmail(email).isDefined) {
      throw new EntryAlreadyExistsException
    }
    val passwordSalt = PasswordUtil.generateRandomSalt
    val passwordHash = PasswordUtil.hashPassword(password, passwordSalt)
    val registerUser = new RegisterUserEvent(firstName, lastName, email, passwordHash, passwordSalt)
    val trigger = triggeredEvents.addNewTriggeredEvent(new UID(0), registerUser)

    sendConfirmRegistrationEmail(email, trigger)

    trigger
  }

  private def sendConfirmRegistrationEmail(email: String, trigger: String) {

    val template = templateRepository.getConfirmRegistrationMail

    val properties = Map[String, String](("link", "http://localhost:8080/socnet/cr/" + trigger))

    mailSender.sendMail("Rejestracja w sieci Socnet", template, email, properties)

  }

  def changeUserEmail(userId: UID, aggregateUserId: UID, version: Int, email: String) {
    val changeEmail = new ChangeEmailEvent(email)
    eventStore.addEvent(new EventRow(userId, aggregateUserId, version, changeEmail))
  }

  def changeUserSummary(userId: UID, aggregateUserId: UID, version: Int, summary: String) {
    val changeSummary = new ChangeSummaryEvent(summary)
    eventStore.addEvent(new EventRow(userId, aggregateUserId, version, changeSummary))
  }


  def createChangeForgottenPasswordTrigger(email: String) {

    val userOption: Option[User] = userDatabase.getUserByEmail(email)

    if (userOption.isDefined) {

      createChangeForgottenPasswordTrigger(userOption.get)

    } else {
      throw new EmailDoesNotExistsException
    }

  }

  private def createChangeForgottenPasswordTrigger(user: User) {
    val changeForgottenPasswordEvent = new ChangeForgottenPasswordEvent()

    val trigger = triggeredEvents.addNewTriggeredEvent(user.id, changeForgottenPasswordEvent)

    sendChangeForgottenPasswordEmail(user.email, trigger)

    trigger

  }

  def sendChangeForgottenPasswordEmail(email: String, trigger: String) {
    val template = templateRepository.getChangeForgottenPasswordMail

    val properties = Map[String, String](("link", "http://localhost:8080/socnet/cfp/" + trigger))

    mailSender.sendMail("Zmiana hasla w sieci Socnet", template, email, properties)
  }

  def triggerChangePassword(userId: UID, aggregateUserId: UID, version: Int, newPassword: String, trigger: String) {

    val eventOption = triggeredEvents.getEventForTrigger(trigger)

    if (eventOption.isEmpty || !eventOption.get.isInstanceOf[ChangeForgottenPasswordEvent]) {
      throw new IncorrectTriggerException
    }

    val changeForgottenPassword = eventOption.get.asInstanceOf[ChangeForgottenPasswordEvent]

    val passwordSalt = PasswordUtil.generateRandomSalt
    val passwordHash = PasswordUtil.hashPassword(newPassword, passwordSalt)

    changeForgottenPassword.passwordSalt = passwordSalt
    changeForgottenPassword.passwordHash = passwordHash

    eventStore.addEvent(new EventRow(userId, aggregateUserId, version, changeForgottenPassword))

    triggeredEvents.markEventAsExecuted(trigger)

  }
}
