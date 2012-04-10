package pl.marpiec.socnet.model

import pl.marpiec.cqrs.CqrsEntity
import userprofile.{JobExperience, Education}
import collection.mutable.ListBuffer

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserProfile extends CqrsEntity(0, 0) {
  var userId:Int = _
  var professionalTitle:String = _
  var city:String = _
  var province:String = _
  var jobExperience = new ListBuffer[JobExperience]
  var education = new ListBuffer[Education]
  var wwwPage:String = _
  var blogPage:String = _

  def createCopy:UserProfile = {
    val profile = new UserProfile
    profile.id = this.id
    profile.version = this.version
    profile.professionalTitle = this.professionalTitle
    profile.city = this.city
    profile.province = this.province
    profile.jobExperience = this.jobExperience.clone
    profile.education = this.education.clone
    profile.wwwPage = this.wwwPage
    profile.blogPage = this.blogPage
    profile
  }
}
