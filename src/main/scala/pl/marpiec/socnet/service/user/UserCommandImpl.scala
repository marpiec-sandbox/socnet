package pl.marpiec.socnet.service.user

import event.{ChangeEmailEvent, RegisterUserEvent}
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.database.UserDatabase
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.database.exception.EntryAlreadyExistsException
import java.util.UUID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserCommandImpl(val eventStore: EventStore, val dataStore: DataStore, val userDatabase: UserDatabase) extends UserCommand {

  override def registerUser(name: String, email: String, password: String): UUID = {
    if (userDatabase.getUserByEmail(email).isDefined) {
      throw new EntryAlreadyExistsException
    }
    //TODO pomyslec o synchronizacji
    val registerUser = new RegisterUserEvent(name, email, password)
    val uuid = UUID.randomUUID()
    eventStore.addEventForNewAggregate(uuid, registerUser)
    uuid
  }

  def changeUserEmail(uuid: UUID, version: Int, email: String) {
    val changeEmail = new ChangeEmailEvent(uuid, version, email)
    eventStore.addEvent(changeEmail);
  }


}
