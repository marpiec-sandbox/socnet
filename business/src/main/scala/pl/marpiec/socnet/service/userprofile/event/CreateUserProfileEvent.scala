package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateUserProfileEvent(userId: UID) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {
    val userProfile = aggregate.asInstanceOf[UserProfile]
    userProfile.userId = userId
  }
}
