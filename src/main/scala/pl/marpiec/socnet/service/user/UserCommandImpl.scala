package pl.marpiec.socnet.service.user

import event.{ChangeEmailEvent, RegisterUserEvent}
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.database.UserDatabase

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserCommandImpl(val eventStore:EventStore, val dataStore:DataStore, val userDatabase:UserDatabase) extends UserCommand {

  override def registerUser(name:String, email:String, password:String):Int = {
    val registerUser = new RegisterUserEvent(name, email, password)
    val id = eventStore.addEventForNewAggregate(registerUser)
    updateUser(id)
    id
  }

  def changeUserEmail(id: Int, version:Int, email: String) {
    val changeEmail = new ChangeEmailEvent(email)
    eventStore.addEvent(id, version, changeEmail);
    updateUser(id)
  }
  
  def updateUser(id:Int) {
    val user = dataStore.getEntity(classOf[User], id).asInstanceOf[User]
    userDatabase.addUser(user)
  }
  
}
