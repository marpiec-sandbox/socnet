package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.util.{BeanUtil, UID}
import pl.marpiec.socnet.model.userprofile.AdditionalInfo

/**
 * @author Marcin Pieciukiewicz
 */

class AddAdditionalInfoEvent(val additionalInfo: AdditionalInfo,
                             val additionalInfoId: UID) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {
    val userProfile = aggregate.asInstanceOf[UserProfile]

    val newAdditionalInfo: AdditionalInfo = BeanUtil.copyProperties(new AdditionalInfo, additionalInfo)

    newAdditionalInfo.id = additionalInfoId

    userProfile.additionalInfo += newAdditionalInfo
  }


}
