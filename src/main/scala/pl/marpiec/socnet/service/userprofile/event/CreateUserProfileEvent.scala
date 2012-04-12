package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateUserProfileEvent(userId:UUID) extends CqrsEvent(null, 0, classOf[UserProfile]) {
  def applyEvent(entity: CqrsEntity) = {
    val userProfile = entity.asInstanceOf[UserProfile]
    userProfile.userId = userId
  }
}