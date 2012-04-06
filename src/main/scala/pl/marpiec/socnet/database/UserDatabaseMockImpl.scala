package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.User
import collection.mutable.HashMap
import pl.marpiec.socnet.service.user.exception.UserAlreadyRegisteredException
import pl.marpiec.util.Strings
import pl.marpiec.cqrs.{CqrsEntity, DataStoreListener, DataStore}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserDatabaseMockImpl(dataStore:DataStore) extends DataStoreListener with UserDatabase {

  private val userDatabase = new HashMap[Int, User]
  private val userByEmailIndex = new HashMap[String, User]

  startListeningToDataStore(dataStore, classOf[User])

  def onEntityChanged(entity: CqrsEntity) {
    val user = entity.asInstanceOf[User]
    if(user.version==1) {
      addUser(user)
    } else {
      updateUser(user)
    }
  }
  
  def getUserById(id: Int):Option[User] = {
    userDatabase.get(id) match {
      case Some(user) => Option.apply(user.createCopy)
      case None => None
    }
  }

  def getUserByEmail(email: String):Option[User] = {
    userByEmailIndex.get(email) match {
      case Some(user) => Option.apply(user.createCopy)
      case None => None
    }
  }

  def addUser(user: User) {
    this.synchronized {
      if(userByEmailIndex.get(user.email).isDefined || userDatabase.get(user.id).isDefined) {
        throw new UserAlreadyRegisteredException
      } else {
        val userCopy: User = user.createCopy
        userDatabase += user.id -> userCopy;
        userByEmailIndex += user.email -> userCopy;
      }
    }
  }

  def updateUser(user: User) {
    this.synchronized {
      val userOption: Option[User] = userDatabase.get(user.id)
      if(userOption.isEmpty) {
        throw new IllegalStateException("No user defined in database, userId="+user.id)
      }
      
      val previousUser = userOption.get
      
      if(Strings.equal(previousUser.email, user.email)) {
        userByEmailIndex.remove(previousUser.email)
      }

      val userCopy: User = user.createCopy
      
      userByEmailIndex += user.email -> userCopy;
      userDatabase += user.id -> userCopy;

    }
  }


}
