package pl.marpiec.socnet.web.page.template

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.panel.Fragment
import pl.marpiec.socnet.web.authorization.AuthorizeUser
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.page.signin.SignInFormPanel
import pl.marpiec.socnet.web.page._
import contacts.{InvitationsReceivedPage, InvitationsSentPage, ContactsPage}
import messages.MessagesPage

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SimpleTemplatePage extends WebPage {

  val titleLabelModel = new Model[String]("Socnet")
  val session: SocnetSession = getSession.asInstanceOf[SocnetSession]

  setStatelessHint(true)
  setVersioned(false)

  add(new BookmarkablePageLink("homeLink", classOf[HomePage]))

  add(new Label("pageTitle", titleLabelModel))


  if (session.isSignedIn) {
    add(new Fragment("userInfo", "loggedUser", this) {
      add(new Label("userName", session.user.fullName))
      add(AuthorizeUser(new BookmarkablePageLink("signoutLink", classOf[SignOutPage])))
      add(AuthorizeUser(new BookmarkablePageLink("messagesLink", classOf[MessagesPage])))
      add(AuthorizeUser(new BookmarkablePageLink("contactsLink", classOf[ContactsPage])))
      add(AuthorizeUser(new BookmarkablePageLink("invitationsSentLink", classOf[InvitationsSentPage])))
      add(AuthorizeUser(new BookmarkablePageLink("invitationsReceivedLink", classOf[InvitationsReceivedPage])))
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
        .add(UserProfilePreviewPage.USER_NAME_PARAM, session.user.fullName)
    } else {
      null
    }
  }
}
