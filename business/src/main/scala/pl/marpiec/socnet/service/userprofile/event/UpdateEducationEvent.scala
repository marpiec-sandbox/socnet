package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.BeanUtil
import pl.marpiec.socnet.model.userprofile.Education

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UpdateEducationEvent(val param: Education) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {

    val userProfile = aggregate.asInstanceOf[UserProfile]

    val educationOption = userProfile.educationById(param.id)

    if (educationOption.isDefined) {
      val education = educationOption.get

      BeanUtil.copyProperties(education, param)

      if (param.stillStudying) {
        education.toYear = 0
        education.toMonthOption = None
      }

    } else {
      throw new IllegalStateException("No Education with given id")
    }


  }

}
