package pl.marpiec.socnet.database

import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.util.UID


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserProfileDatabaseMockImpl(dataStore: DataStore) extends AbstractDatabase[UserProfile](dataStore) with UserProfileDatabase {

  val USER_ID_INDEX = "userId"

  startListeningToDataStore(dataStore, classOf[UserProfile])

  addIndex(USER_ID_INDEX, (aggregate: Aggregate) => {
    val userProfile = aggregate.asInstanceOf[UserProfile]
    userProfile.userId
  });

  def addUserProfile(userProfile: UserProfile) = add(userProfile)

  def updateUserProfile(userProfile: UserProfile) = addOrUpdate(userProfile)

  def getUserProfileByUserId(userId: UID) = getByIndex(USER_ID_INDEX, userId)

  def getUserProfileById(id: UID) = getById(id)
}
