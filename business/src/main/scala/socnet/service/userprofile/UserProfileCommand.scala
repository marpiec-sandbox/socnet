package pl.marpiec.socnet.service.userprofile

import input.{JobExperienceParam, PersonalSummary}
import pl.marpiec.util.UID


/**
 * @author Marcin Pieciukiewicz
 */

trait UserProfileCommand {

  def createUserProfile(userId:UID, id: UID):UID
  def updatePersonalSummary(userId:UID, id: UID, version: Int, personalSummary: PersonalSummary)

  def addJobExperience(userId:UID, id: UID, version: Int, jobExperience: JobExperienceParam, jobExperienceId: UID)
  def updateJobExperience(userId:UID, id: UID, version: Int, jobExperience: JobExperienceParam)
  def removeJobExperience(userId:UID, id: UID, version: Int, jobExperienceId: UID)
}
