package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.socnet.service.userprofile.input.JobExperienceParam
import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.userprofile.JobExperience

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperienceFormModel extends SecureFormModel {

  var id: UID = null
  var companyName: String = ""
  var position: String = ""
  var description: String = ""

  def createJobExperienceParam:JobExperienceParam = {
    val param = new JobExperienceParam
    param.id = this.id
    param.companyName = this.companyName
    param.position = this.position
    param.description = this.description
    param
  }

  def clear() {
    id = null
    companyName = ""
    position = ""
    description = ""
  }
}

object JobExperienceFormModel {

  def apply(param:JobExperience) = {
    val model = new JobExperienceFormModel
    copy(new JobExperienceFormModel, param)
    model
  }

  def copy(to:JobExperienceFormModel, from:JobExperience) {
    to.id = from.id
    to.companyName = from.companyName
    to.position = from.position
    to.description = from.description
  }

  def copy(to:JobExperience, from:JobExperienceFormModel) {
    to.id = from.id
    to.companyName = from.companyName
    to.position = from.position
    to.description = from.description
  }
}