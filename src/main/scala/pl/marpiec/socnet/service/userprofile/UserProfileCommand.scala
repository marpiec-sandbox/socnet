package pl.marpiec.socnet.service.userprofile

import input.{JobExperienceParam, PersonalSummary}
import pl.marpiec.util.UID


/**
 * @author Marcin Pieciukiewicz
 */

trait UserProfileCommand {

  def createUserProfile(userId:UID):UID
  def updatePersonalSummary(id: UID, version: Int, personalSummary: PersonalSummary)

  def addJobExperience(id: UID, version: Int, jobExperience: JobExperienceParam)
  def updateJobExperience(id: UID, version: Int, jobExperience: JobExperienceParam)
  def removeJobExperience(id: UID, version: Int, jobExperienceId: UID)
}
