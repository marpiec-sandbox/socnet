package pl.marpiec.socnet.web.page

import editUserProfilePage.{AdditionalInfoListPanel, EducationListPanel, JobExperienceListPanel, PersonalSummaryPanel}
import pl.marpiec.socnet.database.UserProfileDatabase
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.{SocnetRoles, SocnetSession}


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class EditUserProfilePage extends SecureWebPage(SocnetRoles.USER) {

  //dependencies
  val userProfileCommand: UserProfileCommand = Factory.userProfileCommand
  val userProfileDatabase: UserProfileDatabase = Factory.userProfileDatabase
  val session: SocnetSession = getSession.asInstanceOf[SocnetSession]

  //configure
  setVersioned(false)

  val userProfileOption = userProfileDatabase.getUserProfileByUserId(session.user.id)
  val userProfile = userProfileOption.getOrElse(createUserProfile)


  //schema
  add(new PersonalSummaryPanel("personalSummaryPanel", session.user, userProfile));
  add(new JobExperienceListPanel("jobExperienceListPanel", session.user, userProfile, userProfile.jobExperience))
  add(new EducationListPanel("educationListPanel", session.user, userProfile, userProfile.education))
  add(new AdditionalInfoListPanel("additionalInfoListPanel", session.user, userProfile, userProfile.additionalInfo))


  //methods
  def createUserProfile: UserProfile = {
    val userProfileId = userProfileCommand.createUserProfile(session.user.id, session.user.id)
    val userProfile = new UserProfile
    userProfile.id = userProfileId;
    userProfile.version = 1
    userProfile
  }
}
