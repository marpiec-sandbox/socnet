package pl.marpiec.socnet.readdatabase

import pl.marpiec.cqrs.{Aggregate, DataStore}
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Repository("userProfileDatabase")
class UserProfileDatabaseMockImpl @Autowired() (dataStore: DataStore) extends AbstractDatabase[UserProfile](dataStore) with UserProfileDatabase {

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
