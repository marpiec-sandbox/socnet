package pl.marpiec.socnet.database

import exception.EntryAlreadyExistsException
import pl.marpiec.cqrs.{CqrsEntity, DataStoreListener, DataStore}
import pl.marpiec.socnet.model.UserProfile
import collection.mutable.HashMap


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserProfileDatabaseMockImpl(dataStore: DataStore) extends DataStoreListener with UserProfileDatabase {

  private val userProfileDatabase = new HashMap[Int, UserProfile]
  private val userProfileByUserIdIndex = new HashMap[Int, UserProfile]

  startListeningToDataStore(dataStore, classOf[UserProfile])

  def onEntityChanged(entity: CqrsEntity) {
    val userProfile = entity.asInstanceOf[UserProfile]
    if (userProfile.version == 1) {
      addUserProfile(userProfile)
    } else {
      updateUserProfile(userProfile)
    }
  }

  def addUserProfile(userProfile: UserProfile) {
    this.synchronized {
      if (userProfileDatabase.get(userProfile.id).isDefined) {
        throw new EntryAlreadyExistsException
      } else {
        val userProfileCopy = userProfile.createCopy
        userProfileDatabase += userProfile.id -> userProfileCopy
        userProfileByUserIdIndex += userProfile.userId -> userProfileCopy
      }
    }
  }

  def updateUserProfile(userProfile: UserProfile) {
    this.synchronized {
      val userProfileOption = userProfileDatabase.get(userProfile.id)
      if (userProfileOption.isEmpty) {
        throw new IllegalStateException("No user profile defined in database, userProfileId=" + userProfile.id)
      } else {


        val previousUserProfile = userProfileOption.get

        if (previousUserProfile.userId == userProfile.userId) {
          userProfileByUserIdIndex.remove(previousUserProfile.userId)
        }

        val userProfileCopy = userProfile.createCopy

        userProfileDatabase += userProfile.id -> userProfileCopy
        userProfileByUserIdIndex += userProfile.userId -> userProfileCopy
      }
    }
  }

  def getUserProfileById(id: Int) = {
    userProfileDatabase.get(id) match {
      case Some(user) => Option.apply(user.createCopy)
      case None => None
    }
  }

  def getUserProfileByUserId(userId: Int) = {
    userProfileByUserIdIndex.get(userId) match {
      case Some(user) => Option.apply(user.createCopy)
      case None => None
    }
  }
}
