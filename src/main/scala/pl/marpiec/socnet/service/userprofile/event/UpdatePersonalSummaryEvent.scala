package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.service.userprofile.input.PersonalSummary
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

class UpdatePersonalSummaryEvent(entityId: UUID, expectedVersion: Int, val personalSummary: PersonalSummary)
    extends CqrsEvent(entityId, expectedVersion, classOf[UserProfile]) {
  def applyEvent(entity: CqrsEntity) = {
    val userProfile = entity.asInstanceOf[UserProfile]

    userProfile.professionalTitle = personalSummary.professionalTitle
    userProfile.city = personalSummary.city
    userProfile.province = personalSummary.province
    userProfile.blogPage = personalSummary.blogPage
    userProfile.wwwPage = personalSummary.wwwPage
  }
}
