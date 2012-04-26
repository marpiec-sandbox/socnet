package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.service.userprofile.input.JobExperienceParam
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * @author Marcin Pieciukiewicz
 */

class UpdateJobExperienceEvent(val param: JobExperienceParam) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {

    val userProfile = aggregate.asInstanceOf[UserProfile]

    val jobExperiencOption = userProfile.jobExperienceById(param.id)

    if (jobExperiencOption.isDefined) {
      val jobExperience = jobExperiencOption.get

      jobExperience.id = param.id
      jobExperience.companyName = param.companyName
      jobExperience.fromYear = param.fromYear
      jobExperience.fromMonthOption = param.fromMonthOption
      jobExperience.toYear = param.toYear
      jobExperience.toMonthOption = param.toMonthOption
      jobExperience.currentJob = param.currentJob
      jobExperience.position = param.position
      jobExperience.description = param.description

    } else {
      throw new IllegalStateException("No JobExperience with given id")
    }


  }

}
