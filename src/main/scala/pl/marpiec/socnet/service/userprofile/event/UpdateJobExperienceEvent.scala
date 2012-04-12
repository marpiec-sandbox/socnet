package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.service.userprofile.input.JobExperienceParam
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import pl.marpiec.socnet.model.userprofile.JobExperience

/**
 * @author Marcin Pieciukiewicz
 */

class UpdateJobExperienceEvent(entityId: Int, expectedVersion: Int, val jobExperienceParam: JobExperienceParam)
  extends CqrsEvent(entityId, expectedVersion, classOf[UserProfile]){
  def applyEvent(entity: CqrsEntity) {

    val userProfile = entity.asInstanceOf[UserProfile]

    val jobExperiencOption = userProfile.jobExperienceByUuid(jobExperienceParam.uuid)
    
    if(jobExperiencOption.isDefined) {
      val jobExperience = jobExperiencOption.get

      jobExperience.uuid = jobExperienceParam.uuid
      jobExperience.companyName = jobExperienceParam.companyName
      jobExperience.startDateOption = jobExperienceParam.startDateOption
      jobExperience.endDateOption = jobExperienceParam.endDateOption
      jobExperience.durationMonthsOption = jobExperienceParam.durationMonthsOption
      jobExperience.position = jobExperienceParam.position
      jobExperience.description = jobExperienceParam.description

    } else {
      throw new IllegalStateException("No JobExperience with given uuid")
    }
    

  }

}
