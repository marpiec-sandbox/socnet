package pl.marpiec.socnet.web.page

import editUserProfilePage.elementListPanel.ElementPanel
import editUserProfilePage.{AdditionalInfoListPanel, EducationListPanel, JobExperienceListPanel, PersonalSummaryPanel}
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.socnet.database.UserProfileDatabase
import pl.marpiec.socnet.di.Factory
import pl.marpiec.util.UID
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import collection.mutable.ListBuffer
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.model.userprofile.{Education, JobExperience}
import socnet.model.userprofile.AdditionalInfo
import userProfilePreviewPage._
import pl.marpiec.socnet.model.UserProfile

/**
 * @author Marcin Pieciukiewicz
 */

class UserProfilePreviewPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.NO_ROLES_REQUIRED) {

  //dependencies
  val userProfileDatabase: UserProfileDatabase = Factory.userProfileDatabase
  val userDatabase = Factory.userDatabase

  //configure
  setVersioned(false)

  
  //get data
  val userId = UID.parseOrZero(parameters.get(UserProfilePreviewPage.USER_ID_PARAM).toString)
  
  val userOption = userDatabase.getUserById(userId)
  
  if(userOption.isEmpty) {
    throw new AbortWithHttpErrorCodeException(404);
  }

  val user = userOption.get

  val userProfile = userProfileDatabase.getUserProfileByUserId(userId).getOrElse(new UserProfile)

  //schema
  add(new UserPreviewPanel("userPreviewPanel", user));
  add(new PersonalSummaryPreviewPanel("personalSummaryPreview", userProfile));

  addJobExperienceList(userProfile.jobExperience)
  addEducationList(userProfile.education)
  addAdditionalInfoList(userProfile.additionalInfo)



  //methods
  def addJobExperienceList(jobExperienceList: ListBuffer[JobExperience]) {
    add(new RepeatingView("jobExperiencePreview") {
      for (jobExperience <- jobExperienceList) {
        val item: AbstractItem = new AbstractItem(newChildId());
        item.add(new JobExperiencePreviewPanel("content", jobExperience))
        add(item);
      }
    })
  }

  //methods
  def addEducationList(educationList: ListBuffer[Education]) {
    add(new RepeatingView("educationPreview") {
      for (education <- educationList) {
        val item: AbstractItem = new AbstractItem(newChildId());
        item.add(new EducationPreviewPanel("content", education))
        add(item);
      }
    })
  }

  //methods
  def addAdditionalInfoList(additionalInfoList: ListBuffer[AdditionalInfo]) {
    add(new RepeatingView("additionalInfoPreview") {
      for (additionalInfo <- additionalInfoList) {
        val item: AbstractItem = new AbstractItem(newChildId());
        item.add(new AdditionalInfoPreviewPanel("content", additionalInfo))
        add(item);
      }
    })
  }

}



object UserProfilePreviewPage {
  val USER_ID_PARAM = "k"
}