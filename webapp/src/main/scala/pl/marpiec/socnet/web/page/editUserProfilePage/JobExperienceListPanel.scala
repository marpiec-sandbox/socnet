package pl.marpiec.socnet.web.page.editUserProfilePage

import model.{JobExperienceFormModel, JobExperienceFormModelValidator, JobExperienceDateIModel}
import scala.collection.JavaConversions._
import elementListPanel.ElementListPanel
import pl.marpiec.socnet.model.{UserProfile, User}
import collection.mutable.ListBuffer
import socnet.constant.Month
import org.apache.wicket.markup.html.form._
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.di.Factory
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.userprofile.JobExperience

/**
 * @author Marcin Pieciukiewicz
 */

class JobExperienceListPanel(id: String, user: User, userProfile: UserProfile, jobExperienceList: ListBuffer[JobExperience])
  extends ElementListPanel[JobExperience, JobExperienceFormModel](id, user, userProfile, jobExperienceList) {

  val userProfileCommand = Factory.userProfileCommand

  def createNewElement = new JobExperience

  def buildPreviewSchema(panel: Panel, jobExperience: JobExperience) = {
    panel.add(new Label("companyName", new PropertyModel[String](jobExperience, "companyName")))
    panel.add(new Label("position", new PropertyModel[String](jobExperience, "position")))
    panel.add(new Label("description", new PropertyModel[String](jobExperience, "description")))

    panel.add(new Label("experienceDate", new JobExperienceDateIModel(jobExperience)))
  }

  def buildFormSchema(form: Form[JobExperienceFormModel]) = {
    form.add(new TextField[String]("companyName"))
    form.add(new TextField[String]("position"))
    form.add(new TextArea[String]("description"))
    form.add(new CheckBox("currentJob"))
    form.add(new TextField[Int]("fromYear"))
    form.add(new TextField[Int]("toYear"))

    form.add(new DropDownChoice[Month]("fromMonth", Month.values, new ChoiceRenderer[Month]("translation")))
    form.add(new DropDownChoice[Month]("toMonth", Month.values, new ChoiceRenderer[Month]("translation")))
  }

  def validate(form: JobExperienceFormModel) = {
    JobExperienceFormModelValidator.validate(form)
  }

  def removeElement(element: JobExperience) {
    userProfileCommand.removeJobExperience(user.id, userProfile.id, userProfile.version, element.id)
  }

  def createModelFromElement(element: JobExperience) = {
    JobExperienceFormModel(element)
  }

  def copyElementToModel(model: JobExperienceFormModel, jobExperience: JobExperience) {
    JobExperienceFormModel.copy(model, jobExperience)
  }

  def copyModelToElement(element: JobExperience, model: JobExperienceFormModel) {
    JobExperienceFormModel.copy(element, model)
  }

  def saveNewElement(model: JobExperienceFormModel, newId: UID) {
    val jobExperience = new JobExperience
    JobExperienceFormModel.copy(jobExperience, model)

    userProfileCommand.addJobExperience(user.id, userProfile.id, userProfile.version, jobExperience, newId)
  }

  def saveChangesToElement(model: JobExperienceFormModel) {
    val jobExperience = new JobExperience
    JobExperienceFormModel.copy(jobExperience, model)
    userProfileCommand.updateJobExperience(user.id, userProfile.id, userProfile.version, jobExperience)
  }

  def getPageVariation = "JobExperience"
}
