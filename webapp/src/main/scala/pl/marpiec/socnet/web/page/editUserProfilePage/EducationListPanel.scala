package pl.marpiec.socnet.web.page.editUserProfilePage

import elementListPanel.ElementListPanel
import pl.marpiec.socnet.model.{UserProfile, User}
import collection.mutable.ListBuffer
import pl.marpiec.socnet.model.userprofile.{Education, JobExperience}

/**
 * @author Marcin Pieciukiewicz
 */

class EducationListPanel(id: String, user: User, userProfile: UserProfile, educationList: ListBuffer[Education])
  extends ElementListPanel[Education](id, user, userProfile, educationList) {

}


