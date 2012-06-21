package pl.marpiec.socnet.web.page.profile.editUserProfilePage.model

import pl.marpiec.socnet.service.userprofile.input.PersonalSummary
import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.constant.Province
import pl.marpiec.util.BeanUtil

/**
 * @author Marcin Pieciukiewicz
 */

class PersonalSummaryFormModel extends SecureFormModel {

  var city: String = _
  var province: Province = _
  var wwwPage: String = _
  var blogPage: String = _
  var summary: String = _

  def createPersonalSummary(): PersonalSummary = {
    BeanUtil.copyProperties(new PersonalSummary, this)
  }
}

object PersonalSummaryFormModel {

  def apply(from: UserProfile): PersonalSummaryFormModel = {
    val model = new PersonalSummaryFormModel
    copy(model, from)
    model
  }

  def copy(to: PersonalSummaryFormModel, from: UserProfile) {
    BeanUtil.copyProperties(to, from)
    to.warningMessage = ""
  }

  def copy(to: UserProfile, from: PersonalSummaryFormModel) {
    BeanUtil.copyProperties(to, from)
  }
}
