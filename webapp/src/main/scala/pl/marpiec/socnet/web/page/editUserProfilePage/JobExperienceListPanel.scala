package pl.marpiec.socnet.web.page.editUserProfilePage

import elementListPanel.ElementListPanel
import pl.marpiec.socnet.model.userprofile.JobExperience
import pl.marpiec.socnet.model.{UserProfile, User}
import collection.mutable.ListBuffer

/**
 * @author Marcin Pieciukiewicz
 */

class JobExperienceListPanel(id: String, user: User, userProfile: UserProfile, jobExperienceList: ListBuffer[JobExperience])
  extends ElementListPanel[JobExperience](id, user, userProfile, jobExperienceList) {

}
