package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.socnet.constant.Month
import pl.marpiec.socnet.model.userprofile.Education
import pl.marpiec.util.{Conversion, BeanUtil, UID}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class EducationFormModel extends SecureFormModel {

  var id: UID = null
  var schoolName: String = ""
  var faculty: String = ""
  var level: String = ""
  var major: String = ""

  var fromYear: String = ""
  var fromMonth: Month = null
  var toYear: String = ""
  var toMonth: Month = null

  var stillStudying: Boolean = false

  var description: String = ""

}

object EducationFormModel {
  def apply(param: Education) = {
    val model = new EducationFormModel
    copy(model, param)
    model
  }

  def copy(to: EducationFormModel, from: Education): EducationFormModel = {
    BeanUtil.copyProperties(to, from)

    to.fromYear = Conversion.emptyIfZero(from.fromYear)
    to.fromMonth = from.fromMonthOption.getOrElse(null)

    if (from.stillStudying) {
      to.toYear = ""
      to.toMonth = null
    } else {
      to.toYear = Conversion.emptyIfZero(from.toYear)
      to.toMonth = from.toMonthOption.getOrElse(null)
    }
    to
  }

  def copy(to: Education, from: EducationFormModel): Education = {

    BeanUtil.copyProperties(to, from)

    to.fromYear = from.fromYear.toInt
    to.fromMonthOption = Option(from.fromMonth)

    if (to.stillStudying) {
      to.toYear = 0
      to.toMonthOption = None
    } else {
      to.toYear = from.toYear.toInt
      to.toMonthOption = Option(from.toMonth)
    }
    to
  }


}