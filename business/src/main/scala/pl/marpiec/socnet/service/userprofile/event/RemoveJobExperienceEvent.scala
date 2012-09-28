package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.util.UID
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * @author Marcin Pieciukiewicz
 */

class RemoveJobExperienceEvent(val jobExperienceId: UID) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {
    val userProfile = aggregate.asInstanceOf[UserProfile]

    val jobExperienceOption = userProfile.jobExperienceById(jobExperienceId)

    if (jobExperienceOption.isDefined) {
      val jobExperience = jobExperienceOption.get
      userProfile.jobExperience = userProfile.jobExperience.filterNot(je => je.id == jobExperience.id)
    } else {
      throw new IllegalStateException("No JobExperience with given uid")
    }
  }
}
