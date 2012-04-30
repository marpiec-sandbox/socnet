package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.util.{BeanUtil, UID}
import socnet.model.userprofile.AdditionalInfo

/**
 * @author Marcin Pieciukiewicz
 */

class AdditionalInfoFormModel extends SecureFormModel {

  var id: UID = null

  var title: String = ""
  var description: String = ""

}

object AdditionalInfoFormModel {
  def apply(param: AdditionalInfo) = {
    val model = new AdditionalInfoFormModel
    copy(model, param)
    model
  }

  def copy(to: AdditionalInfoFormModel, from: AdditionalInfo): AdditionalInfoFormModel = {
    BeanUtil.copyProperties(to, from)
  }

  def copy(to: AdditionalInfo, from: AdditionalInfoFormModel): AdditionalInfo = {
    BeanUtil.copyProperties(to, from)
    to
  }
}
