package pl.marpiec.socnet.web.page

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.util.UID
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import collection.mutable.ListBuffer
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.model.userprofile.{Education, JobExperience}
import socnet.model.userprofile.AdditionalInfo
import userProfilePreviewPage._
import pl.marpiec.socnet.readdatabase.{UserDatabase, UserProfileDatabase}
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.model.{User, UserProfile}
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.component.contacts.PersonContactInfo
import pl.marpiec.socnet.web.application.{SocnetSession, SocnetRoles}
import socnet.model.UserContacts
import socnet.readdatabase.UserContactsDatabase
import pl.marpiec.socnet.web.component.conversation.StartConversationPanel

/**
 * @author Marcin Pieciukiewicz
 */

class UserProfilePreviewPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.NO_ROLES_REQUIRED) {

  //dependencies
  @SpringBean
  private var userProfileDatabase: UserProfileDatabase = _
  @SpringBean
  private var userDatabase: UserDatabase = _
  @SpringBean
  private var userContactsDatabase: UserContactsDatabase = _


  //get data
  val userId = UID.parseOrZero(parameters.get(UserProfilePreviewPage.USER_ID_PARAM).toString)

  val userOption = userDatabase.getUserById(userId)

  if (userOption.isEmpty) {
    throw new AbortWithHttpErrorCodeException(404);
  }

  val user = userOption.get

  val userProfile = userProfileDatabase.getUserProfileByUserId(userId).getOrElse(new UserProfile)


  val currentUserContacts = userContactsDatabase.getUserContactsByUserId(session.userId).getOrElse(new UserContacts)

  //schema
  add(new UserPreviewPanel("userPreviewPanel", user));
  add(new PersonalSummaryPreviewPanel("personalSummaryPreview", userProfile));

  addJobExperienceList(userProfile.jobExperience)
  addEducationList(userProfile.education)
  addAdditionalInfoList(userProfile.additionalInfo)

  add(new PersonContactInfo("personContactInfo", user.id, currentUserContacts))
  add(new StartConversationPanel("startConversationPanel", user.id))

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
  val USER_ID_PARAM = "userId"
  val USER_NAME_PARAM = "userName"

  def getLink(user:User):BookmarkablePageLink[_] = {
    new BookmarkablePageLink("profileLink", classOf[UserProfilePreviewPage], getParametersForLink(user))
  }
  
  def getParametersForLink(user:User):PageParameters = {
    new PageParameters().add(USER_ID_PARAM, user.id).add(USER_NAME_PARAM, user.fullName)
  }
}