package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.service.userprofile.input.JobExperienceParam
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import pl.marpiec.socnet.model.userprofile.JobExperience
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class UpdateJobExperienceEvent(entityId: UID, expectedVersion: Int, val jobExperienceParam: JobExperienceParam)
  extends CqrsEvent(entityId, expectedVersion){

  def entityClass = classOf[UserProfile]

  def applyEvent(entity: CqrsEntity) {

    val userProfile = entity.asInstanceOf[UserProfile]

    val jobExperiencOption = userProfile.jobExperienceById(jobExperienceParam.id)
    
    if(jobExperiencOption.isDefined) {
      val jobExperience = jobExperiencOption.get

      jobExperience.id = jobExperienceParam.id
      jobExperience.companyName = jobExperienceParam.companyName
      jobExperience.startDateOption = jobExperienceParam.startDateOption
      jobExperience.endDateOption = jobExperienceParam.endDateOption
      jobExperience.durationMonthsOption = jobExperienceParam.durationMonthsOption
      jobExperience.position = jobExperienceParam.position
      jobExperience.description = jobExperienceParam.description

    } else {
      throw new IllegalStateException("No JobExperience with given id")
    }
    

  }

}
