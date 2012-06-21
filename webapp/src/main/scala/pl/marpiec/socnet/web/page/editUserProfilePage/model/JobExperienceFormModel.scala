package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.socnet.model.userprofile.JobExperience
import pl.marpiec.socnet.constant.Month
import pl.marpiec.util.{Conversion, BeanUtil, UID}

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

}

object JobExperienceFormModel {

  def apply(param: JobExperience) = {
    val model = new JobExperienceFormModel
    copy(model, param)
    model
  }

  def copy(to: JobExperienceFormModel, from: JobExperience): JobExperienceFormModel = {
    BeanUtil.copyProperties(to, from)

    to.fromYear = Conversion.emptyIfZero(from.fromYear)
    to.fromMonth = from.fromMonthOption.getOrElse(null)

    if (from.currentJob) {
      to.toYear = ""
      to.toMonth = null
    } else {
      to.toYear = Conversion.emptyIfZero(from.toYear)
      to.toMonth = from.toMonthOption.getOrElse(null)
    }
    to
  }

  def copy(to: JobExperience, from: JobExperienceFormModel): JobExperience = {
    BeanUtil.copyProperties(to, from)

    to.fromYear = from.fromYear.toInt
    to.fromMonthOption = Option(from.fromMonth)
    if (to.currentJob) {
      to.toYear = 0
      to.toMonthOption = None
    } else {
      to.toYear = from.toYear.toInt
      to.toMonthOption = Option(from.toMonth)
    }
    to
  }

}