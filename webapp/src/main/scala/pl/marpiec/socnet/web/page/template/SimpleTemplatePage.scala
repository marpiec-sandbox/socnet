package pl.marpiec.socnet.web.page.template

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.authroles.authentication.panel.SignInPanel
import pl.marpiec.socnet.web.authorization.AuthorizeUser
import pl.marpiec.socnet.web.page.{UserProfilePreviewPage, EditUserProfilePage, HomePage, SignOutPage}
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.page.signin.SignInFormPanel

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SimpleTemplatePage extends WebPage {

  val titleLabelModel = new Model[String]("Socnet")
  private val session: SocnetSession = getSession.asInstanceOf[SocnetSession]

  setStatelessHint(true)

  add(new BookmarkablePageLink("homeLink", classOf[HomePage]))

  add(new Label("pageTitle", titleLabelModel))


  if (session.isSignedIn) {
    add(new Fragment("userInfo", "loggedUser", this) {
      add(new Label("userName", session.user.fullName))
      add(AuthorizeUser(new BookmarkablePageLink("signoutLink", classOf[SignOutPage])))
      add(AuthorizeUser(new BookmarkablePageLink("editProfileLink", classOf[EditUserProfilePage])))
      add(AuthorizeUser(new BookmarkablePageLink("previewProfileLink", classOf[UserProfilePreviewPage], createParametersForProfilePreview)))
    })
  } else {
    add(new Fragment("userInfo", "userNotLoggedIn", this) {
      add(new SignInFormPanel("signInPanel"))
    })
  }


  protected def setSubTitle(title: String) {
    titleLabelModel.setObject("Socnet " + title)
  }

  private def createParametersForProfilePreview: PageParameters = {
    if (session.isAuthenticated()) {
      new PageParameters()
        .add(UserProfilePreviewPage.USER_ID_PARAM, session.userId())
        .add("n", session.user.fullName)
    } else {
      null
    }
  }
}
