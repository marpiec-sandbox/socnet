package pl.marpiec.socnet.web.page.profile

import pl.marpiec.util.UID
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import collection.mutable.ListBuffer
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.model.userprofile.{Education, JobExperience}
import pl.marpiec.socnet.model.userprofile.AdditionalInfo
import pl.marpiec.socnet.readdatabase.{UserDatabase, UserProfileDatabase}
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.model.{User, UserProfile}
import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.readdatabase.UserContactsDatabase
import pl.marpiec.socnet.web.component.conversation.StartConversationPanel
import pl.marpiec.socnet.web.page.profile.userProfilePreviewPage._
import pl.marpiec.socnet.web.authorization.{AuthorizeUser, SecureWebPage}
import pl.marpiec.socnet.web.component.contacts.{PersonContactLevelPanel, PersonContactInvitationPanel}

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


  val loggedInUserContacts:UserContacts = userContactsDatabase.getUserContactsByUserId(session.userId).getOrElse(new UserContacts)
  val userContacts:UserContacts = userContactsDatabase.getUserContactsByUserId(userId).getOrElse(new UserContacts)

  //schema
  add(new UserPreviewPanel("userPreviewPanel", user));
  add(new PersonalSummaryPreviewPanel("personalSummaryPreview", userProfile));

  addJobExperienceList(userProfile.jobExperience)
  addEducationList(userProfile.education)
  addAdditionalInfoList(userProfile.additionalInfo)

  add(AuthorizeUser(new PersonContactInvitationPanel("personContactInfo", user.id, loggedInUserContacts)))
  add(AuthorizeUser(new StartConversationPanel("startConversationPanel", user.id).setVisible(user.id != session.userId)))
  add(AuthorizeUser(new PersonContactLevelPanel("personContactLevelPanel", user.id, userContacts, session.userId, loggedInUserContacts)))

  add(new UserContactsPreviewPanel("userContactsPreviewPanel", userContacts, loggedInUserContacts))

  //methods
  def addJobExperienceList(jobExperienceList: ListBuffer[JobExperience]) {
    add(new RepeatingView("jobExperiencePreview") {
      setVisible(jobExperienceList.size > 0)

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
      setVisible(educationList.size > 0)

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
      setVisible(additionalInfoList.size > 0)

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

  def getLink(user: User): BookmarkablePageLink[_] = {
    new BookmarkablePageLink("profileLink", classOf[UserProfilePreviewPage], getParametersForLink(user))
  }

  def getParametersForLink(user: User): PageParameters = {
    new PageParameters().add(USER_ID_PARAM, user.id).add(USER_NAME_PARAM, user.fullNameForUrl)
  }
}