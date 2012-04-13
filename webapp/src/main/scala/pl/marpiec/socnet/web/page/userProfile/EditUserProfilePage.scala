package pl.marpiec.socnet.web.page.userProfile

import component.{JobExperienceListPanel, PersonalSummaryPanel}
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.database.UserProfileDatabase
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.UserProfile
import org.apache.wicket.markup.html.WebPage
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import pl.marpiec.socnet.model.userprofile.JobExperience

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class EditUserProfilePage extends WebPage {

  setVersioned(false)

  val userProfileCommand: UserProfileCommand = Factory.userProfileCommand
  val userProfileDatabase: UserProfileDatabase = Factory.userProfileDatabase
  val session: SocnetSession = getSession.asInstanceOf[SocnetSession]

  val userProfileOption = userProfileDatabase.getUserProfileByUserId(session.user.id)
  val userProfile = userProfileOption.getOrElse(createUserProfile)

  add(new PersonalSummaryPanel("personalSummaryPanel", session.user, userProfile));
  add(new JobExperienceListPanel("jobExperienceListPanel", session.user, userProfile, userProfile.jobExperience))

  def createUserProfile: UserProfile = {
    val userProfileId = userProfileCommand.createUserProfile(session.user.id)
    val userProfile = new UserProfile
    userProfile.id = userProfileId;
    userProfile.version = 1
    userProfile
  }
}
