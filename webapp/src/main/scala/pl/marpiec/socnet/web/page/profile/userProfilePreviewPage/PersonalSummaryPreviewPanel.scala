package pl.marpiec.socnet.web.page.profile.userProfilePreviewPage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.UserProfile
import org.apache.wicket.markup.html.link.ExternalLink
import org.apache.wicket.markup.html.basic.{MultiLineLabel, Label}
import org.apache.commons.lang.StringUtils
import pl.marpiec.util.UrlUtil

/**
 * @author Marcin Pieciukiewicz
 */

class PersonalSummaryPreviewPanel(id: String, val userProfile: UserProfile) extends Panel(id) {

  //schema

  add(new Label("city", userProfile.city).setVisible(StringUtils.isNotBlank(userProfile.city)))
  val province = getProvinceOrNull
  add(new Label("province", province).setVisible(StringUtils.isNotBlank(province)))
  
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
    add(new ExternalLink(id, UrlUtil.addHttpIfNoProtocol(link), UrlUtil.removeProtocol(link)).setVisible(StringUtils.isNotBlank(link)))
  }

}
