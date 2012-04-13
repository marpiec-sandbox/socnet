package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.util.UID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserProfileDatabase {
  def addUserProfile(userProfile: UserProfile)
  def updateUserProfile(userProfile: UserProfile)
  def getUserProfileByUserId(userId:UID):Option[UserProfile]
  def getUserProfileById(id: UID):Option[UserProfile]
}
