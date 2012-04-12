package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.service.userprofile.input.{JobExperienceParam, PersonalSummary}
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import pl.marpiec.socnet.model.userprofile.JobExperience
import java.util.UUID
import org.joda.time.LocalDate

/**
 * @author Marcin Pieciukiewicz
 */

class AddJobExperienceEvent(entityId: UUID, expectedVersion: Int, val jobExperienceParam: JobExperienceParam)
  extends CqrsEvent(entityId, expectedVersion, classOf[UserProfile]){

  def applyEvent(entity: CqrsEntity) {
    val userProfile = entity.asInstanceOf[UserProfile]

    val jobExperience = new JobExperience
    jobExperience.uuid = jobExperienceParam.uuid
    jobExperience.companyName = jobExperienceParam.companyName
    jobExperience.startDateOption = jobExperienceParam.startDateOption
    jobExperience.endDateOption = jobExperienceParam.endDateOption
    jobExperience.durationMonthsOption = jobExperienceParam.durationMonthsOption
    jobExperience.position = jobExperienceParam.position
    jobExperience.description = jobExperienceParam.description

    userProfile.jobExperience += jobExperience
  }
}
