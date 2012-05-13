package pl.marpiec.socnet.web.page.editUserProfilePage

import model._
import scala.collection.JavaConversions._
import elementListPanel.ElementListPanel
import pl.marpiec.socnet.model.{UserProfile, User}
import collection.mutable.ListBuffer
import socnet.constant.Month
import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.util.UID
import org.apache.wicket.markup.html.form._
import org.apache.wicket.model.PropertyModel
import pl.marpiec.socnet.model.userprofile.Education
import org.apache.wicket.markup.html.basic.{MultiLineLabel, Label}
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import org.apache.wicket.spring.injection.annot.SpringBean

/**
 * @author Marcin Pieciukiewicz
 */

class EducationListPanel(id: String, user: User, userProfile: UserProfile, educationList: ListBuffer[Education])
  extends ElementListPanel[Education, EducationFormModel](id, user, userProfile, educationList) {

  @SpringBean
  var userProfileCommand: UserProfileCommand = _

  def createNewElement = new Education

  def buildPreviewSchema(panel: Panel, education: Education) = {
    panel.add(new Label("schoolName", new PropertyModel[String](education, "schoolName")))
    panel.add(new Label("faculty", new PropertyModel[String](education, "faculty")))
    panel.add(new Label("major", new PropertyModel[String](education, "major")))
    panel.add(new Label("level", new PropertyModel[String](education, "level")))
    panel.add(new MultiLineLabel("description", new PropertyModel[String](education, "description")))

    panel.add(new Label("educationDate", new EducationDateIModel(education)))
  }

  def buildFormSchema(form: Form[EducationFormModel]) = {
    form.add(new TextField[String]("schoolName"))
    form.add(new TextField[String]("faculty"))
    form.add(new TextField[String]("major"))
    form.add(new TextField[String]("level"))

    form.add(new TextArea[String]("description"))
    form.add(new CheckBox("stillStudying"))
    form.add(new TextField[Int]("fromYear"))
    form.add(new TextField[Int]("toYear"))

    form.add(new DropDownChoice[Month]("fromMonth", Month.values, new ChoiceRenderer[Month]("translation")))
    form.add(new DropDownChoice[Month]("toMonth", Month.values, new ChoiceRenderer[Month]("translation")))
  }

  def validate(form: EducationFormModel) = {
    EducationFormModelValidator.validate(form)
  }

  def removeElement(element: Education) {
    userProfileCommand.removeEducation(user.id, userProfile.id, userProfile.version, element.id)
  }

  def createModelFromElement(element: Education) = {
    EducationFormModel(element)
  }

  def copyElementToModel(model: EducationFormModel, education: Education) {
    EducationFormModel.copy(model, education)
  }

  def copyModelToElement(element: Education, model: EducationFormModel) {
    EducationFormModel.copy(element, model)
  }

  def saveNewElement(model: EducationFormModel, newId: UID) {
    val education = EducationFormModel.copy(new Education, model)

    userProfileCommand.addEducation(user.id, userProfile.id, userProfile.version, education, newId)
  }

  def saveChangesToElement(model: EducationFormModel) {

    val education = EducationFormModel.copy(new Education, model)
    userProfileCommand.updateEducation(user.id, userProfile.id, userProfile.version, education)
  }

  def getPageVariation = "Education"
}
