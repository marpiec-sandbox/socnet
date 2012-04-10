package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.UserProfile

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserProfileDatabase {
  def addUserProfile(userProfile: UserProfile)
  def updateUserProfile(userProfile: UserProfile)
  def getUserProfileByUserId(userId:Int):Option[UserProfile]
  def getUserProfileById(id: Int):Option[UserProfile]
}
