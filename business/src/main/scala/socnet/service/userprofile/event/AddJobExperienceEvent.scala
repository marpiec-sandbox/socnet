package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.service.userprofile.input.JobExperienceParam
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.userprofile.JobExperience
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class AddJobExperienceEvent(val jobExperienceParam: JobExperienceParam,
                            val jobExperienceId: UID) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {
    val userProfile = aggregate.asInstanceOf[UserProfile]

    val jobExperience = new JobExperience
    jobExperience.id = jobExperienceId
    jobExperience.companyName = jobExperienceParam.companyName
    jobExperience.fromYear = jobExperienceParam.fromYear
    jobExperience.fromMonthOption = jobExperienceParam.fromMonthOption
    jobExperience.toYear = jobExperienceParam.toYear
    jobExperience.toMonthOption = jobExperienceParam.toMonthOption
    jobExperience.currentJob = jobExperience.currentJob
    jobExperience.position = jobExperienceParam.position
    jobExperience.description = jobExperienceParam.description

    userProfile.jobExperience += jobExperience
  }


}
