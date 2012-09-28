package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.userprofile.JobExperience
import pl.marpiec.util.{BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class AddJobExperienceEvent(val jobExperience: JobExperience,
                            val jobExperienceId: UID) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {
    val userProfile = aggregate.asInstanceOf[UserProfile]

    val newExperience: JobExperience = BeanUtil.copyProperties(new JobExperience, jobExperience)
    newExperience.id = jobExperienceId

    userProfile.jobExperience ::= newExperience
  }


}
