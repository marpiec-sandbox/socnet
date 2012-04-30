package pl.marpiec.socnet.web.page.editUserProfilePage

import model._
import elementListPanel.ElementListPanel
import pl.marpiec.socnet.model.{UserProfile, User}
import collection.mutable.ListBuffer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.di.Factory
import pl.marpiec.util.UID
import org.apache.wicket.markup.html.form._
import org.apache.wicket.model.PropertyModel
import socnet.model.userprofile.AdditionalInfo

/**
 * @author Marcin Pieciukiewicz
 */

class AdditionalInfoListPanel(id: String, user: User, userProfile: UserProfile, additionalInfoList: ListBuffer[AdditionalInfo])
  extends ElementListPanel[AdditionalInfo, AdditionalInfoFormModel](id, user, userProfile, additionalInfoList) {

  val userProfileCommand = Factory.userProfileCommand

  def createNewElement = new AdditionalInfo

  def buildPreviewSchema(panel: Panel, additionalInfo: AdditionalInfo) = {
    panel.add(new Label("title", new PropertyModel[String](additionalInfo, "title")))
    panel.add(new Label("description", new PropertyModel[String](additionalInfo, "description")))
  }

  def buildFormSchema(form: Form[AdditionalInfoFormModel]) = {
    form.add(new TextField[String]("title"))
    form.add(new TextArea[String]("description"))
  }

  def validate(form: AdditionalInfoFormModel) = {
    AdditionalInfoFormModelValidator.validate(form)
  }

  def removeElement(additionalInfo: AdditionalInfo) {
    userProfileCommand.removeAdditionalInfo(user.id, userProfile.id, userProfile.version, additionalInfo.id)
  }

  def createModelFromElement(element: AdditionalInfo) = {
    AdditionalInfoFormModel(element)
  }

  def copyElementToModel(model: AdditionalInfoFormModel, additionalInfo: AdditionalInfo) {
    AdditionalInfoFormModel.copy(model, additionalInfo)
  }

  def copyModelToElement(element: AdditionalInfo, model: AdditionalInfoFormModel) {
    AdditionalInfoFormModel.copy(element, model)
  }

  def saveNewElement(model: AdditionalInfoFormModel, newId: UID) {
    val additionalInfo = AdditionalInfoFormModel.copy(new AdditionalInfo, model)
    userProfileCommand.addAdditionalInfo(user.id, userProfile.id, userProfile.version, additionalInfo, newId)
  }

  def saveChangesToElement(model: AdditionalInfoFormModel) {
    val additionalInfo = AdditionalInfoFormModel.copy(new AdditionalInfo, model)
    userProfileCommand.updateAdditionalInfo(user.id, userProfile.id, userProfile.version, additionalInfo)
  }

  def getPageVariation = "AdditionalInfo"
}
