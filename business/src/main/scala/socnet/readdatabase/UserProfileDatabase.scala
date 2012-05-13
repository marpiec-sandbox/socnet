package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{User, UserProfile}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserProfileDatabase {


  def addUserProfile(userProfile: UserProfile)
  def updateUserProfile(userProfile: UserProfile)
  def getUserProfileByUserId(userId:UID):Option[UserProfile]
  def getUserProfileById(id: UID):Option[UserProfile]
  def getUserProfiles(list: List[User]):Map[User, UserProfile]
}
