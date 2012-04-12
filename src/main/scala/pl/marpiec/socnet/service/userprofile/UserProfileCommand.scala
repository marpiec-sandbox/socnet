package pl.marpiec.socnet.service.userprofile

import input.{JobExperienceParam, PersonalSummary}
import java.util.UUID


/**
 * @author Marcin Pieciukiewicz
 */

trait UserProfileCommand {

  def createUserProfile(userId:UUID):UUID
  def updatePersonalSummary(id: UUID, version: Int, personalSummary: PersonalSummary)

  def addJobExperience(id: UUID, version: Int, jobExperience: JobExperienceParam)
  def updateJobExperience(id: UUID, version: Int, jobExperience: JobExperienceParam)
  def removeJobExperience(id: UUID, version: Int, jobExperienceUuid: UUID)
}
