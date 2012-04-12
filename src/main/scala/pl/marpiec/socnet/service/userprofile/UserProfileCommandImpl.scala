package pl.marpiec.socnet.service.userprofile

import event._
import input.{JobExperienceParam, PersonalSummary}
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.service.article.event.CreateArticleEvent
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

class UserProfileCommandImpl(val eventStore: EventStore, val dataStore: DataStore) extends UserProfileCommand {
  def createUserProfile(userId: UUID):UUID = {
    val createUserProfile = new CreateUserProfileEvent(userId)
    val uuid = UUID.randomUUID()
    eventStore.addEventForNewAggregate(uuid, createUserProfile)
    uuid
  }

  def updatePersonalSummary(uuid: UUID, version: Int, personalSummary: PersonalSummary) {
    eventStore.addEvent(new UpdatePersonalSummaryEvent(uuid, version, personalSummary))
  }

  def addJobExperience(uuid: UUID, version: Int, jobExperience: JobExperienceParam) {
    eventStore.addEvent(new AddJobExperienceEvent(uuid, version, jobExperience))
  }

  def updateJobExperience(uuid: UUID, version: Int, jobExperience: JobExperienceParam) {
    eventStore.addEvent(new UpdateJobExperienceEvent(uuid, version, jobExperience))
  }

  def removeJobExperience(uuid: UUID, version: Int, jobExperienceUuid: UUID) {
    eventStore.addEvent(new RemoveJobExperienceEvent(uuid, version, jobExperienceUuid))
  }
}
