package pl.marpiec.socnet.service.userprofile

import event.{CreateUserProfileEvent, UpdatePersonalSummaryEvent}
import input.PersonalSummary
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.service.article.event.CreateArticleEvent

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
    val updatePersonalSummary = new UpdatePersonalSummaryEvent(id, version, personalSummary)
    eventStore.addEvent(updatePersonalSummary)
  }
}
