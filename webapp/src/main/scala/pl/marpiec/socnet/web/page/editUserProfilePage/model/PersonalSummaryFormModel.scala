package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.socnet.service.userprofile.input.PersonalSummary
import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.socnet.model.UserProfile
import socnet.constant.Province

/**
 * @author Marcin Pieciukiewicz
 */

class PersonalSummaryFormModel extends SecureFormModel {
  var professionalTitle: String = _
  var city: String = _
  var province: Province = _
  var wwwPage: String = _
  var blogPage: String = _
  var summary: String = _

  def createPersonalSummary():PersonalSummary = {
    val summary = new PersonalSummary
    summary.professionalTitle = this.professionalTitle
    summary.city = this.city
    summary.province = this.province
    summary.wwwPage = this.wwwPage
    summary.blogPage = this.blogPage
    summary.summary = this.summary
    summary
  }

  def clear() {
    professionalTitle = ""
    city = ""
    province = null
    wwwPage = ""
    blogPage = ""
    summary = ""
  }
}

object PersonalSummaryFormModel {

  def apply(from: UserProfile):PersonalSummaryFormModel = {
    val model = new PersonalSummaryFormModel
    copy(model, from)
    model
  }

  def copy(to: PersonalSummaryFormModel, from: UserProfile) {
    to.warningMessage = ""
    to.professionalTitle = from.professionalTitle
    to.city = from.city
    to.province = from.province
    to.wwwPage = from.wwwPage
    to.blogPage = from.blogPage
    to.summary = from.summary
  }

  def copy(to: UserProfile, from: PersonalSummaryFormModel) {
    to.professionalTitle = from.professionalTitle
    to.city = from.city
    to.province = from.province
    to.wwwPage = from.wwwPage
    to.blogPage = from.blogPage
    to.summary = from.summary
  }
}
