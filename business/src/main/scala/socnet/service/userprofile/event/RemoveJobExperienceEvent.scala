package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}

/**
 * @author Marcin Pieciukiewicz
 */

class RemoveJobExperienceEvent(entityId: UID, expectedVersion: Int, val jobExperienceId: UID)
  extends CqrsEvent(entityId, expectedVersion) {

  def entityClass = classOf[UserProfile]

  def applyEvent(entity: CqrsEntity) {
    val userProfile = entity.asInstanceOf[UserProfile]

    val jobExperiencOption = userProfile.jobExperienceById(jobExperienceId)

    if (jobExperiencOption.isDefined) {
      val jobExperience = jobExperiencOption.get
      userProfile.jobExperience -= jobExperience
    } else {
      throw new IllegalStateException("No JobExperience with given uid")
    }
  }
}
