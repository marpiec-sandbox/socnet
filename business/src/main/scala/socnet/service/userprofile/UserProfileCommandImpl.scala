package pl.marpiec.socnet.service.userprofile

import event._
import input.{JobExperienceParam, PersonalSummary}
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Event, UidGenerator, DataStore, EventStore}

/**
 * @author Marcin Pieciukiewicz
 */

class UserProfileCommandImpl(val eventStore: EventStore, val dataStore: DataStore, val uidGenerator:UidGenerator) extends UserProfileCommand {
  def createUserProfile(userId:UID, userAggregateId: UID): UID = {
    val createUserProfile = new CreateUserProfileEvent(userAggregateId)
    val id = uidGenerator.nextUid
    eventStore.addEventForNewAggregate(id, new Event(userId, id, 0, createUserProfile))
    id
  }

  def updatePersonalSummary(userId:UID, id: UID, version: Int, personalSummary: PersonalSummary) {
    eventStore.addEvent(new Event(userId, id, version, new UpdatePersonalSummaryEvent(personalSummary)))
  }

  def addJobExperience(userId:UID, id: UID, version: Int, jobExperience: JobExperienceParam, jobExperienceId: UID) {
    eventStore.addEvent(new Event(userId, id, version, new AddJobExperienceEvent(jobExperience, jobExperienceId)))
  }

  def updateJobExperience(userId:UID, id: UID, version: Int, jobExperience: JobExperienceParam) {
    eventStore.addEvent(new Event(userId, id, version, new UpdateJobExperienceEvent(jobExperience)))
  }

  def removeJobExperience(userId:UID, id: UID, version: Int, jobExperienceId: UID) {
    eventStore.addEvent(new Event(userId, id, version, new RemoveJobExperienceEvent(jobExperienceId)))
  }
}
