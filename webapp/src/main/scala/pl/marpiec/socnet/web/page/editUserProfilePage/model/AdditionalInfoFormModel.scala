package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.socnet.web.wicket.SecureFormModel
import socnet.model.userprofile.AdditionalInfo
import socnet.constant.Month
import pl.marpiec.util.{Conversion, BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class AdditionalInfoFormModel extends SecureFormModel {

  var id: UID = null

  var title: String = ""
  var description: String = ""

  var fromYear: String = ""
  var fromMonth: Month = null
  var toYear: String = ""
  var toMonth: Month = null

  var oneDate: Boolean = false

}

object AdditionalInfoFormModel {
  def apply(param: AdditionalInfo) = {
    val model = new AdditionalInfoFormModel
    copy(model, param)
    model
  }

  def copy(to: AdditionalInfoFormModel, from: AdditionalInfo): AdditionalInfoFormModel = {
    BeanUtil.copyProperties(to, from)
    to.fromYear = Conversion.emptyIfZero(from.fromYear)
    to.fromMonth = from.fromMonthOption.getOrElse(null)

    if (from.oneDate) {
      to.toYear = ""
      to.toMonth = null
    } else {
      to.toYear = Conversion.emptyIfZero(from.toYear)
      to.toMonth = from.toMonthOption.getOrElse(null)
    }
    to
  }

  def copy(to: AdditionalInfo, from: AdditionalInfoFormModel): AdditionalInfo = {
    BeanUtil.copyProperties(to, from)

    to.fromYear = from.fromYear.toInt
    to.fromMonthOption = Option(from.fromMonth)

    if (to.oneDate) {
      to.toYear = 0
      to.toMonthOption = None
    } else {
      to.toYear = from.toYear.toInt
      to.toMonthOption = Option(from.toMonth)
    }
    to
  }
}
