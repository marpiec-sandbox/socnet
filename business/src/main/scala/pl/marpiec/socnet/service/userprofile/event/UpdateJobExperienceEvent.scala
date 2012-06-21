package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.userprofile.JobExperience
import pl.marpiec.util.BeanUtil

/**
 * @author Marcin Pieciukiewicz
 */

class UpdateJobExperienceEvent(val param: JobExperience) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {

    val userProfile = aggregate.asInstanceOf[UserProfile]

    val jobExperiencOption = userProfile.jobExperienceById(param.id)

    if (jobExperiencOption.isDefined) {
      val jobExperience = jobExperiencOption.get

      BeanUtil.copyProperties(jobExperience, param)

      if (param.currentJob) {
        jobExperience.toYear = 0
        jobExperience.toMonthOption = None
      }

    } else {
      throw new IllegalStateException("No JobExperience with given id")
    }


  }

}
