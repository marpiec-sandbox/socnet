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
  def createUserProfile(userId: Int) = {
    val createUserProfile = new CreateUserProfileEvent(userId)
    val id = eventStore.addEventForNewAggregate(createUserProfile)
    id
  }

  def updatePersonalSummary(id: Int, version: Int, personalSummary: PersonalSummary) {
    eventStore.addEvent(new UpdatePersonalSummaryEvent(id, version, personalSummary))
  }

  def addJobExperience(id: Int, version: Int, jobExperience: JobExperienceParam) {
    eventStore.addEvent(new AddJobExperienceEvent(id, version, jobExperience))
  }

  def updateJobExperience(id: Int, version: Int, jobExperience: JobExperienceParam) {
    eventStore.addEvent(new UpdateJobExperienceEvent(id, version, jobExperience))
  }

  def removeJobExperience(id: Int, version: Int, jobExperienceUuid: UUID) {
    eventStore.addEvent(new RemoveJobExperienceEvent(id, version, jobExperienceUuid))
  }
}
