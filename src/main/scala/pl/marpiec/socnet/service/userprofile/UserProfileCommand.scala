package pl.marpiec.socnet.service.userprofile

import input.{JobExperienceParam, PersonalSummary}
import java.util.UUID


/**
 * @author Marcin Pieciukiewicz
 */

trait UserProfileCommand {

  def createUserProfile(userId:Int):Int
  def updatePersonalSummary(id: Int, version: Int, personalSummary: PersonalSummary)

  def addJobExperience(id: Int, version: Int, jobExperience: JobExperienceParam)
  def updateJobExperience(id: Int, version: Int, jobExperience: JobExperienceParam)
  def removeJobExperience(id: Int, version: Int, jobExperienceUuid: UUID)
}
