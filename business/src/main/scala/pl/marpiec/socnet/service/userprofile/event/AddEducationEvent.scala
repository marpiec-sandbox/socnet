package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.{BeanUtil, UID}
import pl.marpiec.socnet.model.userprofile.Education

/**
 * @author Marcin Pieciukiewicz
 */

class AddEducationEvent(val education: Education,
                        val educationId: UID) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {
    val userProfile = aggregate.asInstanceOf[UserProfile]

    val newEducation: Education = BeanUtil.copyProperties(new Education, education)

    newEducation.id = educationId

    userProfile.education += newEducation
  }


}
