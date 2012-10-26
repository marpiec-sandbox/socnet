package pl.marpiec.socnet.web.page.profile

import pl.marpiec.util.UID
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.model.userprofile.{Education, JobExperience}
import pl.marpiec.socnet.model.userprofile.AdditionalInfo
import pl.marpiec.socnet.readdatabase.{UserDatabase, UserProfileDatabase}
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.model.{User, UserProfile}
import pl.marpiec.socnet.constant.SocnetRoles
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.readdatabase.UserContactsDatabase
import pl.marpiec.socnet.web.page.profile.userProfilePreviewPage._
import pl.marpiec.socnet.web.authorization.{AuthorizeUser, SecureWebPage}
import pl.marpiec.socnet.web.component.usertechnologies.UserTechnologiesPreviewPanel
import pl.marpiec.socnet.web.page.usertechnologies.UserTechnologiesPage
import pl.marpiec.socnet.web.component.contacts.PersonContactPanel

/**
 * @author Marcin Pieciukiewicz
 */

class UserProfilePreviewPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  //dependencies
  @SpringBean private var userProfileDatabase: UserProfileDatabase = _
  @SpringBean private var userDatabase: UserDatabase = _
  @SpringBean private var userContactsDatabase: UserContactsDatabase = _


  //get data
  val userId = UID.parseOrZero(parameters.get(UserProfilePreviewPage.USER_ID_PARAM).toString)

  val user = userDatabase.getUserById(userId).getOrElse(throw new AbortWithHttpErrorCodeException(404))

  val userProfile = userProfileDatabase.getUserProfileByUserId(userId).getOrElse(new UserProfile)


  val loggedInUserContacts: UserContacts = userContactsDatabase.getUserContactsByUserId(session.userId).getOrElse(new UserContacts)
  val userContacts: UserContacts = userContactsDatabase.getUserContactsByUserId(userId).getOrElse(new UserContacts)

  val itsCurrentserProfile = user.id == session.userId

  //schema

  add(AuthorizeUser(new BookmarkablePageLink("editProfileLink", classOf[EditUserProfilePage])).setVisible(itsCurrentserProfile))
  add(AuthorizeUser(new BookmarkablePageLink("technologiesLink", classOf[UserTechnologiesPage]).setVisible(itsCurrentserProfile)))

  add(new UserPreviewPanel("userPreviewPanel", user));
  add(new PersonalSummaryPreviewPanel("personalSummaryPreview", userProfile));

  addJobExperienceList(userProfile.jobExperience)
  addEducationList(userProfile.education)
  addAdditionalInfoList(userProfile.additionalInfo)

  add(AuthorizeUser(new PersonContactPanel("personContactPanel", user.id, loggedInUserContacts, loggedInUserContacts)))

  add(new UserContactsPreviewPanel("userContactsPreviewPanel", userContacts, loggedInUserContacts, itsCurrentserProfile))

  add(new UserTechnologiesPreviewPanel("knownTechnologies", user.id))

  //methods
  def addJobExperienceList(jobExperienceList: List[JobExperience]) {
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
  def addEducationList(educationList: List[Education]) {
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
  def addAdditionalInfoList(additionalInfoList: List[AdditionalInfo]) {
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