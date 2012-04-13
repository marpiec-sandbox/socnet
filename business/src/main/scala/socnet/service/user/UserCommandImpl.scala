package pl.marpiec.socnet.service.user

import event.{ChangeEmailEvent, RegisterUserEvent}
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.database.UserDatabase
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.database.exception.EntryAlreadyExistsException
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserCommandImpl(val eventStore: EventStore, val dataStore: DataStore, val userDatabase: UserDatabase) extends UserCommand {

  override def registerUser(name: String, email: String, password: String): UID = {
    if (userDatabase.getUserByEmail(email).isDefined) {
      throw new EntryAlreadyExistsException
    }
    //TODO pomyslec o synchronizacji
    val registerUser = new RegisterUserEvent(name, email, password)
    val id = UID.generate
    eventStore.addEventForNewAggregate(id, registerUser)
    id
  }

  def changeUserEmail(id: UID, version: Int, email: String) {
    val changeEmail = new ChangeEmailEvent(id, version, email)
    eventStore.addEvent(changeEmail);
  }


}