package pl.marpiec.socnet.service.userprofile

import event._
import input.{JobExperienceParam, PersonalSummary}
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{UidGenerator, DataStore, EventStore}

/**
 * @author Marcin Pieciukiewicz
 */

class UserProfileCommandImpl(val eventStore: EventStore, val dataStore: DataStore, val uidGenerator:UidGenerator) extends UserProfileCommand {
  def createUserProfile(userId: UID): UID = {
    val createUserProfile = new CreateUserProfileEvent(userId)
    val id = uidGenerator.nextUid
    eventStore.addEventForNewAggregate(id, createUserProfile)
    id
  }

  def updatePersonalSummary(id: UID, version: Int, personalSummary: PersonalSummary) {
    eventStore.addEvent(new UpdatePersonalSummaryEvent(id, version, personalSummary))
  }

  def addJobExperience(id: UID, version: Int, jobExperience: JobExperienceParam) {
    eventStore.addEvent(new AddJobExperienceEvent(id, version, jobExperience))
  }

  def updateJobExperience(id: UID, version: Int, jobExperience: JobExperienceParam) {
    eventStore.addEvent(new UpdateJobExperienceEvent(id, version, jobExperience))
  }

  def removeJobExperience(id: UID, version: Int, jobExperienceId: UID) {
    eventStore.addEvent(new RemoveJobExperienceEvent(id, version, jobExperienceId))
  }
}
