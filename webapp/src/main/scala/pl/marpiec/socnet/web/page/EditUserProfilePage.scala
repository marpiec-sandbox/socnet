package pl.marpiec.socnet.web.page

import editUserProfilePage.{AdditionalInfoListPanel, EducationListPanel, JobExperienceListPanel, PersonalSummaryPanel}
import pl.marpiec.socnet.readdatabase.UserProfileDatabase
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.{SocnetRoles, SocnetSession}
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.cqrs.UidGenerator


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class EditUserProfilePage extends SecureWebPage(SocnetRoles.USER) {

  //dependencies
  @SpringBean
  var userProfileCommand: UserProfileCommand = _
  @SpringBean
  var userProfileDatabase: UserProfileDatabase = _
  @SpringBean
  var uidGenerator:UidGenerator = _

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
    val userProfileId = uidGenerator.nextUid
    userProfileCommand.createUserProfile(session.user.id, session.user.id, userProfileId)
    val userProfile = new UserProfile
    userProfile.id = userProfileId;
    userProfile.version = 1
    userProfile
  }
}
