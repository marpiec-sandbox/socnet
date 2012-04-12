package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import java.util.UUID
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}

/**
 * @author Marcin Pieciukiewicz
 */

class RemoveJobExperienceEvent(entityId: UUID, expectedVersion: Int, val jobExperienceUuid: UUID)
  extends CqrsEvent(entityId, expectedVersion, classOf[UserProfile]) {
  def applyEvent(entity: CqrsEntity) {
    val userProfile = entity.asInstanceOf[UserProfile]

    val jobExperiencOption = userProfile.jobExperienceByUuid(jobExperienceUuid)

    if (jobExperiencOption.isDefined) {
      val jobExperience = jobExperiencOption.get
      userProfile.jobExperience -= jobExperience
    } else {
      throw new IllegalStateException("No JobExperience with given uuid")
    }
  }
}
