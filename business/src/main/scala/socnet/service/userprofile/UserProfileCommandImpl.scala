package pl.marpiec.socnet.service.userprofile

import event._
import input.{JobExperienceParam, PersonalSummary}
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.service.article.event.CreateArticleEvent
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class UserProfileCommandImpl(val eventStore: EventStore, val dataStore: DataStore) extends UserProfileCommand {
  def createUserProfile(userId: UID):UID = {
    val createUserProfile = new CreateUserProfileEvent(userId)
    val id = UID.generate
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
