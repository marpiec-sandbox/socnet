package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.UserProfile
import java.util.UUID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UserProfileDatabase {
  def addUserProfile(userProfile: UserProfile)
  def updateUserProfile(userProfile: UserProfile)
  def getUserProfileByUserId(userId:UUID):Option[UserProfile]
  def getUserProfileById(id: UUID):Option[UserProfile]
}
