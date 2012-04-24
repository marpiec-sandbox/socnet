package pl.marpiec.socnet.service.userprofile.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.service.userprofile.input.PersonalSummary

/**
 * @author Marcin Pieciukiewicz
 */

class UpdatePersonalSummaryEvent(val personalSummary: PersonalSummary) extends Event {

  def entityClass = classOf[UserProfile]

  def applyEvent(aggregate: Aggregate) = {
    val userProfile = aggregate.asInstanceOf[UserProfile]

    userProfile.professionalTitle = personalSummary.professionalTitle
    userProfile.city = personalSummary.city
    userProfile.province = personalSummary.province
    userProfile.blogPage = personalSummary.blogPage
    userProfile.wwwPage = personalSummary.wwwPage
    userProfile.summary = personalSummary.summary
  }
}
