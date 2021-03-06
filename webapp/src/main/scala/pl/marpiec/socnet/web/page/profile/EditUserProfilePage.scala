package pl.marpiec.socnet.web.page.profile

import editUserProfilePage._
import pl.marpiec.socnet.readdatabase.UserProfileDatabase
import pl.marpiec.socnet.service.userprofile.UserProfileCommand
import pl.marpiec.socnet.constant.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.cqrs.UidGenerator
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.usertechnologies.UserTechnologiesPage
import pl.marpiec.socnet.web.authorization.{AuthorizeUser, SecureWebPage}


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class EditUserProfilePage extends SecureWebPage(SocnetRoles.USER) {

  //dependencies
  @SpringBean private var userProfileCommand: UserProfileCommand = _
  @SpringBean private var userProfileDatabase: UserProfileDatabase = _
  @SpringBean private var uidGenerator: UidGenerator = _


  val userProfileOption = userProfileDatabase.getUserProfileByUserId(session.user.id)
  val userProfile = userProfileOption.getOrElse(throw new IllegalStateException("No user profile defined"))


  //schema
  
  add(UserProfilePreviewPage.getLink("profileLink", session.user))
  add(AuthorizeUser(new BookmarkablePageLink("technologiesLink", classOf[UserTechnologiesPage])))
  
  add(new UserSummaryPanel("userSummaryPanel", session.user));
  add(new PersonalSummaryPanel("personalSummaryPanel", session.user, userProfile));
  add(new JobExperienceListPanel("jobExperienceListPanel", session.user, userProfile, userProfile.jobExperience))
  add(new EducationListPanel("educationListPanel", session.user, userProfile, userProfile.education))
  add(new AdditionalInfoListPanel("additionalInfoListPanel", session.user, userProfile, userProfile.additionalInfo))


}
