package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.profile.{UserProfilePreviewPage, EditUserProfilePage}
import pl.marpiec.socnet.web.authorization.AuthorizeUser
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class ProfileModule(id:String) extends Panel(id) {

  val session = getSession.asInstanceOf[SocnetSession]

  if(session.isAuthenticated) {

    add(new Label("userName", session.user.fullName))
    add(new Label("professionalTitle", session.user.summary))

    add(AuthorizeUser(new BookmarkablePageLink("editProfileLink", classOf[EditUserProfilePage])))
    add(AuthorizeUser(UserProfilePreviewPage.getLink(session.user)))
  }





}
