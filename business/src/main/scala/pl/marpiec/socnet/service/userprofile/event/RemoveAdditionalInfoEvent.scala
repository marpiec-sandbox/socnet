package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{Aggregate, Event}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class RemoveAdditionalInfoEvent(val additionalInfoId: UID) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) {
    val userProfile = aggregate.asInstanceOf[UserProfile]

    val additionalInfoOption = userProfile.additionalInfoById(additionalInfoId)

    if (additionalInfoOption.isDefined) {
      val additionalInfo = additionalInfoOption.get
      userProfile.additionalInfo -= additionalInfo
    } else {
      throw new IllegalStateException("No AdditionalInfo with given uid")
    }
  }
}