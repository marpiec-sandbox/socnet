package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class RemoveEducationEvent(val educationId: UID) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {
    val userProfile = aggregate.asInstanceOf[UserProfile]

    val educationOption = userProfile.educationById(educationId)

    if (educationOption.isDefined) {
      val education = educationOption.get
      userProfile.education = userProfile.education.filterNot(e => e.id == education.id)
    } else {
      throw new IllegalStateException("No Education with given uid")
    }
  }
}