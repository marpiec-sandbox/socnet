package pl.marpiec.socnet.service.user

import event.{ChangeEmailEvent, RegisterUserEvent}
import exception.UserAlreadyRegisteredException
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.database.UserDatabase

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserCommandImpl(val eventStore:EventStore, val dataStore:DataStore, val userDatabase:UserDatabase) extends UserCommand {

  override def registerUser(name:String, email:String, password:String):Int = {
    if(userDatabase.getUserByEmail(email).isDefined) {
      throw new UserAlreadyRegisteredException
    }
    //TODO pomyslec o synchronizacji
    val registerUser = new RegisterUserEvent(name, email, password)
    val id = eventStore.addEventForNewAggregate(registerUser)
    addUserToDatabase(id)
    id
  }

  def changeUserEmail(id: Int, version:Int, email: String) {
    val changeEmail = new ChangeEmailEvent(id, version, email)
    eventStore.addEvent(changeEmail);
    updateUserInDatabase(id)
  }

  def addUserToDatabase(id:Int) {
    val user = dataStore.getEntity(classOf[User], id).asInstanceOf[User]
    userDatabase.addUser(user)
  }

  def updateUserInDatabase(id:Int) {
    val user = dataStore.getEntity(classOf[User], id).asInstanceOf[User]
    userDatabase.updateUser(user)
  }
  
}
