package pl.marpiec.socnet.service.user

import event.{ChangeEmailEvent, RegisterUserEvent}
import pl.marpiec.socnet.database.UserDatabase
import pl.marpiec.socnet.database.exception.EntryAlreadyExistsException
import pl.marpiec.cqrs._
import exception.IncorrectTriggerException
import pl.marpiec.mailsender.MailSender
import socnet.template.TemplateRepository
import pl.marpiec.util.{TemplateUtil, PasswordUtil, UID}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserCommandImpl(val eventStore: EventStore, val dataStore: DataStore,
                      val triggeredEvents:TriggeredEvents, val userDatabase: UserDatabase,
                      val uidGenerator:UidGenerator, mailSender:MailSender, templateRepository:TemplateRepository) extends UserCommand {

  
  override def triggerUserRegistration(trigger: String):UID = {

    val eventOption = triggeredEvents.getEventForTrigger(trigger)
    
    if(eventOption.isEmpty || !eventOption.get.isInstanceOf[RegisterUserEvent]) {
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

    sendEmailWithTrigger(email, trigger)

    trigger
  }

  private def sendEmailWithTrigger(email: String, trigger: String) {

    val template = templateRepository.getConfirmRegistrationMail
    
    val properties = Map[String, String](("link", "http://localhost:8080/socnet/cr?t="+trigger))

    mailSender.sendMail("Rejestracja w sieci Socnet", template, email, properties)
    
  }

  def changeUserEmail(userId:UID, aggregateUserId: UID, version: Int, email: String) {
    val changeEmail = new ChangeEmailEvent(email)
    eventStore.addEvent(new EventRow(userId, aggregateUserId, version, changeEmail))
  }


}
