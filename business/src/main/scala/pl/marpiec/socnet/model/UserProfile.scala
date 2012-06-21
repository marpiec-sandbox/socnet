package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import userprofile.{JobExperience, Education}
import collection.mutable.ListBuffer
import pl.marpiec.socnet.constant.Province
import pl.marpiec.socnet.model.userprofile.AdditionalInfo
import pl.marpiec.util.{BeanUtil, UID}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UserProfile extends Aggregate(null, 0) {


  var userId: UID = _
  var city: String = _
  var province: Province = _
  var jobExperience = new ListBuffer[JobExperience]
  var education = new ListBuffer[Education]
  var additionalInfo = new ListBuffer[AdditionalInfo]
  var wwwPage: String = _
  var blogPage: String = _
  var summary: String = _

  def jobExperienceById(uid: UID): Option[JobExperience] = jobExperience.find(exp => exp.id == uid)

  def educationById(uid: UID): Option[Education] = education.find(edu => edu.id == uid)

  def additionalInfoById(uid: UID): Option[AdditionalInfo] = additionalInfo.find(info => info.id == uid)

  def copy: Aggregate = {
    BeanUtil.copyProperties(new UserProfile, this)
  }
}
