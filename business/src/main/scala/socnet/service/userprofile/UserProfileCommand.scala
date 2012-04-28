package pl.marpiec.socnet.service.userprofile

import input.PersonalSummary
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.userprofile.{Education, JobExperience}


/**
 * @author Marcin Pieciukiewicz
 */

trait UserProfileCommand {

  def createUserProfile(userId:UID, id: UID):UID
  def updatePersonalSummary(userId:UID, id: UID, version: Int, personalSummary: PersonalSummary)

  def addJobExperience(userId:UID, id: UID, version: Int, jobExperience: JobExperience, jobExperienceId: UID)
  def updateJobExperience(userId:UID, id: UID, version: Int, jobExperience: JobExperience)
  def removeJobExperience(userId:UID, id: UID, version: Int, jobExperienceId: UID)

  def addEducation(userId: UID, id: UID, version: Int, education: Education, educationId: UID)
  def updateEducation(userId: UID, id: UID, version: Int, education: Education)
  def removeEducation(userId: UID, id: UID, version: Int, educationId: UID)
}
