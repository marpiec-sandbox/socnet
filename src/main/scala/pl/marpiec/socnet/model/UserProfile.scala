package pl.marpiec.socnet.model

import pl.marpiec.cqrs.CqrsEntity
import userprofile.{JobExperience, Education}
import collection.mutable.ListBuffer
import java.util.UUID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserProfile extends CqrsEntity(null, 0) {
  var userId:UUID = _
  var professionalTitle:String = _
  var city:String = _
  var province:String = _
  var jobExperience = new ListBuffer[JobExperience]
  var education = new ListBuffer[Education]
  var wwwPage:String = _
  var blogPage:String = _
  var summary:String = _
  
  def jobExperienceByUuid(uuid:UUID):Option[JobExperience] = jobExperience.find(exp => exp.uuid == uuid)

  def copy:CqrsEntity = {
    val profile = new UserProfile
    profile.uuid = this.uuid
    profile.version = this.version
    profile.professionalTitle = this.professionalTitle
    profile.city = this.city
    profile.province = this.province
    profile.jobExperience = this.jobExperience.clone
    profile.education = this.education.clone
    profile.wwwPage = this.wwwPage
    profile.blogPage = this.blogPage
    profile.summary = this.summary
    profile
  }
}
