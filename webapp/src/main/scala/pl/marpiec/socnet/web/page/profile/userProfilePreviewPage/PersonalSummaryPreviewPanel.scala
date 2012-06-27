package pl.marpiec.socnet.web.page.profile.userProfilePreviewPage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.UserProfile
import org.apache.wicket.markup.html.link.ExternalLink
import org.apache.wicket.markup.html.basic.{MultiLineLabel, Label}
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

class PersonalSummaryPreviewPanel(id: String, val userProfile: UserProfile) extends Panel(id) {

  //schema

  add(new Label("city", userProfile.city))
  add(new Label("province", getProvinceOrNull))
  
  addWwwLink("wwwPage", userProfile.wwwPage)
  addWwwLink("blogPage", userProfile.blogPage)
  
  add(new MultiLineLabel("summary", userProfile.summary))

  private def getProvinceOrNull: String = {
    if (userProfile.province == null) {
      null
    } else {
      userProfile.province.translation
    }
  }
  
  private def addWwwLink(id:String, link:String) {
    add(new ExternalLink(id, "http://" + link, link).setVisible(StringUtils.isNotBlank(link)))
  }

}
