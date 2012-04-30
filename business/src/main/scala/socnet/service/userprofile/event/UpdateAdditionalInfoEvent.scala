package socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.BeanUtil
import socnet.model.userprofile.AdditionalInfo

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UpdateAdditionalInfoEvent(val param: AdditionalInfo) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {

    val userProfile = aggregate.asInstanceOf[UserProfile]

    val additionalInfoOption = userProfile.additionalInfoById(param.id)

    if (additionalInfoOption.isDefined) {
      val additionalInfo = additionalInfoOption.get

      BeanUtil.copyProperties(additionalInfo, param)

      if (param.oneDate) {
        param.toYear = 0
        param.toMonthOption = None
      }

    } else {
      throw new IllegalStateException("No AdditionalInfo with given id")
    }


  }

}
