package pl.marpiec.socnet.web.page.profile.editUserProfilePage.model

import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.socnet.model.User
import pl.marpiec.util.BeanUtil

/**
 * @author Marcin Pieciukiewicz
 */

class UserSummaryFormModel extends SecureFormModel {
  var firstName: String = _
  var lastName: String = _
  var summary: String = _

}


object UserSummaryFormModel {

  def apply(from: User): UserSummaryFormModel = {
    val model = new UserSummaryFormModel
    BeanUtil.copyProperties(model, from)
    model
  }
}