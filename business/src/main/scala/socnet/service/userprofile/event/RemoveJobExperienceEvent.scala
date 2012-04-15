package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}

/**
 * @author Marcin Pieciukiewicz
 */

class RemoveJobExperienceEvent(val jobExperienceId: UID) extends CqrsEvent {

  def entityClass = classOf[UserProfile]

  def applyEvent(entity: CqrsEntity) {
    val userProfile = entity.asInstanceOf[UserProfile]

    val jobExperienceOption = userProfile.jobExperienceById(jobExperienceId)

    if (jobExperienceOption.isDefined) {
      val jobExperience = jobExperienceOption.get
      userProfile.jobExperience -= jobExperience
    } else {
      throw new IllegalStateException("No JobExperience with given uid")
    }
  }
}
