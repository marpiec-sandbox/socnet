package pl.marpiec.socnet.service.userprofile

import event._
import input.PersonalSummary
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{EventRow, UidGenerator, DataStore, EventStore}
import pl.marpiec.socnet.model.userprofile.{Education, JobExperience}
import socnet.model.userprofile.AdditionalInfo
import socnet.service.userprofile.event._

/**
 * @author Marcin Pieciukiewicz
 */

class UserProfileCommandImpl(val eventStore: EventStore, val dataStore: DataStore, val uidGenerator:UidGenerator) extends UserProfileCommand {
  def createUserProfile(userId:UID, userAggregateId: UID): UID = {
    val createUserProfile = new CreateUserProfileEvent(userAggregateId)
    val id = uidGenerator.nextUid
    eventStore.addEventForNewAggregate(id, new EventRow(userId, id, 0, createUserProfile))
    id
  }

  def updatePersonalSummary(userId:UID, id: UID, version: Int, personalSummary: PersonalSummary) {
    eventStore.addEvent(new EventRow(userId, id, version, new UpdatePersonalSummaryEvent(personalSummary)))
  }

  def addJobExperience(userId:UID, id: UID, version: Int, jobExperience: JobExperience, jobExperienceId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new AddJobExperienceEvent(jobExperience, jobExperienceId)))
  }

  def updateJobExperience(userId:UID, id: UID, version: Int, jobExperience: JobExperience) {
    eventStore.addEvent(new EventRow(userId, id, version, new UpdateJobExperienceEvent(jobExperience)))
  }

  def removeJobExperience(userId:UID, id: UID, version: Int, jobExperienceId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new RemoveJobExperienceEvent(jobExperienceId)))
  }

  def addEducation(userId: UID, id: UID, version: Int, education: Education, educationId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new AddEducationEvent(education, educationId)))
  }

  def updateEducation(userId: UID, id: UID, version: Int, education: Education) {
    eventStore.addEvent(new EventRow(userId, id, version, new UpdateEducationEvent(education)))
  }

  def removeEducation(userId: UID, id: UID, version: Int, educationId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new RemoveEducationEvent(educationId)))
  }

  def addAdditionalInfo(userId: UID, id: UID, version: Int, additionalInfo: AdditionalInfo, additionalInfoId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new AddAdditionalInfoEvent(additionalInfo, additionalInfoId)))
  }

  def updateAdditionalInfo(userId: UID, id: UID, version: Int, additionalInfo: AdditionalInfo) {
    eventStore.addEvent(new EventRow(userId, id, version, new UpdateAdditionalInfoEvent(additionalInfo)))
  }

  def removeAdditionalInfo(userId: UID, id: UID, version: Int, additionalInfoId: UID) {
    eventStore.addEvent(new EventRow(userId, id, version, new RemoveAdditionalInfoEvent(additionalInfoId)))
  }
}
