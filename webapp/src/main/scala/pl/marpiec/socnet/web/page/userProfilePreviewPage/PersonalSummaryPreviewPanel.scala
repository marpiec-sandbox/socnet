package pl.marpiec.socnet.web.page.userProfilePreviewPage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.{UserProfile, User}
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class PersonalSummaryPreviewPanel(id: String, val userProfile: UserProfile) extends Panel(id) {
  
  //schema
  add(new Label("professionalTitle", userProfile.professionalTitle))
  add(new Label("city", userProfile.city+", woj. "+userProfile.province))
  add(new Label("wwwPage", userProfile.wwwPage))
  add(new Label("blogPage", userProfile.blogPage))
  add(new Label("summary", userProfile.summary))
  
}
