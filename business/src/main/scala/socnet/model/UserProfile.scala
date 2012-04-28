package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import userprofile.{JobExperience, Education}
import collection.mutable.ListBuffer
import pl.marpiec.util.UID
import socnet.constant.Province

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserProfile extends Aggregate(null, 0) {


  var userId:UID = _
  var professionalTitle:String = _
  var city:String = _
  var province:Province = _
  var jobExperience = new ListBuffer[JobExperience]
  var education = new ListBuffer[Education]
  var wwwPage:String = _
  var blogPage:String = _
  var summary:String = _
  
  def jobExperienceById(id:UID):Option[JobExperience] = jobExperience.find(exp => exp.id == id)

  def educationById(uid: UID):Option[Education] = education.find(exp => exp.id == id)

  def copy:Aggregate = {
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
    profile.summary = this.summary
    profile
  }
}
