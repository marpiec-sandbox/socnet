package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.service.userprofile.input.{JobExperienceParam, PersonalSummary}
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import pl.marpiec.socnet.model.userprofile.JobExperience
import pl.marpiec.util.UID
import org.joda.time.LocalDate

/**
 * @author Marcin Pieciukiewicz
 */

class AddJobExperienceEvent(entityId: UID, expectedVersion: Int, val jobExperienceParam: JobExperienceParam)
  extends CqrsEvent(entityId, expectedVersion){

  def entityClass = classOf[UserProfile]

  def applyEvent(entity: CqrsEntity) {
    val userProfile = entity.asInstanceOf[UserProfile]

    val jobExperience = new JobExperience
    jobExperience.id = jobExperienceParam.id
    jobExperience.companyName = jobExperienceParam.companyName
    jobExperience.startDateOption = jobExperienceParam.startDateOption
    jobExperience.endDateOption = jobExperienceParam.endDateOption
    jobExperience.durationMonthsOption = jobExperienceParam.durationMonthsOption
    jobExperience.position = jobExperienceParam.position
    jobExperience.description = jobExperienceParam.description

    userProfile.jobExperience += jobExperience
  }


}
