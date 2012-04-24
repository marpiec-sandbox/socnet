package pl.marpiec.socnet.service.user

import event.{ChangeEmailEvent, RegisterUserEvent}
import pl.marpiec.socnet.database.UserDatabase
import pl.marpiec.socnet.database.exception.EntryAlreadyExistsException
import pl.marpiec.cqrs._
import pl.marpiec.util.{PasswordUtil, UID}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserCommandImpl(val eventStore: EventStore, val dataStore: DataStore, val userDatabase: UserDatabase, val uidGenerator:UidGenerator) extends UserCommand {

  override def registerUser(firstName: String, lastName:String, email: String, password: String): UID = {
    if (userDatabase.getUserByEmail(email).isDefined) {
      throw new EntryAlreadyExistsException
    }
    //TODO pomyslec o synchronizacji

    val passwordSalt = PasswordUtil.generateRandomSalt
    val passwordHash = PasswordUtil.hashPassword(password, passwordSalt)

    val registerUser = new RegisterUserEvent(firstName, lastName, email, passwordHash, passwordSalt)
    val id = uidGenerator.nextUid
    eventStore.addEventForNewAggregate(id, new EventRow(id, id, 0, registerUser))
    id
  }

  def changeUserEmail(userId:UID, aggregateUserId: UID, version: Int, email: String) {
    val changeEmail = new ChangeEmailEvent(email)
    eventStore.addEvent(new EventRow(userId, aggregateUserId, version, changeEmail))
  }


}
