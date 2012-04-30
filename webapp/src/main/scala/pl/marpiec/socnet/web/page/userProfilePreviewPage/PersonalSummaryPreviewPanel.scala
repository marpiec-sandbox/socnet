package pl.marpiec.socnet.web.page.userProfilePreviewPage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.{UserProfile, User}
import org.apache.wicket.markup.html.link.ExternalLink
import org.apache.wicket.markup.html.basic.{MultiLineLabel, Label}

/**
 * @author Marcin Pieciukiewicz
 */

class PersonalSummaryPreviewPanel(id: String, val userProfile: UserProfile) extends Panel(id) {
  
  //schema
  add(new Label("professionalTitle", userProfile.professionalTitle))
  add(new Label("city", userProfile.city+", woj. "+userProfile.province.translation))
  add(new ExternalLink("wwwPage", "http://"+userProfile.wwwPage, userProfile.wwwPage))
  add(new ExternalLink("blogPage", "http://"+userProfile.blogPage, userProfile.blogPage))
  add(new MultiLineLabel("summary", userProfile.summary))
  
}
