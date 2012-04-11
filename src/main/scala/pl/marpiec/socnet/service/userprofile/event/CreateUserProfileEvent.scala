package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}

/**
 * @author Marcin Pieciukiewicz
 */

class CreateUserProfileEvent(userId:Int) extends CqrsEvent(0, 0, classOf[UserProfile]) {
  def applyEvent(entity: CqrsEntity) = {
    val userProfile = entity.asInstanceOf[UserProfile]
    userProfile.userId = userId
  }
}
