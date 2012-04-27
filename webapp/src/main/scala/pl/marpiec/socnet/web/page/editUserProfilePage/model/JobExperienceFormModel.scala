package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.socnet.service.userprofile.input.JobExperienceParam
import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.userprofile.JobExperience
import socnet.constant.Month
import org.joda.time.LocalDate

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperienceFormModel extends SecureFormModel {

  var id: UID = null
  var companyName: String = ""
  var position: String = ""
  var description: String = ""
  
  var currentJob: Boolean = false

  var fromMonth: Month = null
  var fromYear: String = _
  
  var toMonth: Month = null
  var toYear: String = _

  def createJobExperienceParam:JobExperienceParam = {
    val param = new JobExperienceParam
    param.id = this.id
    param.companyName = this.companyName
    param.position = this.position
    param.description = this.description
    param.fromYear = this.fromYear.toInt
    param.fromMonthOption = Option(this.fromMonth)
    param.currentJob = this.currentJob
    if(currentJob) {
      param.toYear = 0
      param.toMonthOption = None
    } else {
      param.toYear = this.toYear.toInt
      param.toMonthOption = Option(this.toMonth)
    }

    
    param
  }

  def clear() {
    id = null
    companyName = ""
    position = ""
    description = ""
    currentJob = false
    fromMonth = null
    fromYear = ""
    toMonth = null
    toYear = ""
  }
}

object JobExperienceFormModel {

  def apply(param:JobExperience) = {
    val model = new JobExperienceFormModel
    copy(model, param)
    model
  }

  def copy(to:JobExperienceFormModel, from:JobExperience) {
    to.id = from.id
    to.companyName = from.companyName
    to.position = from.position
    to.description = from.description
    to.currentJob = from.currentJob
    
    to.fromYear = from.fromYear.toString
    to.fromMonth = from.fromMonthOption.getOrElse(null)
    
    if(from.currentJob) {
      to.toYear = ""
      to.toMonth = null
    } else {
      to.toYear = from.toYear.toString
      to.toMonth = from.toMonthOption.getOrElse(null)
    }
  }

  def copy(to:JobExperience, from:JobExperienceFormModel) {
    to.id = from.id
    to.companyName = from.companyName
    to.position = from.position
    to.description = from.description
    to.fromYear = from.fromYear.toInt
    to.fromMonthOption = Option(from.fromMonth)
    to.currentJob = from.currentJob
    if(to.currentJob) {
      to.toYear = 0
      to.toMonthOption = None
    } else {
      to.toYear = from.toYear.toInt
      to.toMonthOption = Option(from.toMonth)
    }
  }
}