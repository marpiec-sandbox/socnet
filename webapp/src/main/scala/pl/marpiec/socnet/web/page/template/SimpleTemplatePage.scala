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
import conversation.UserConversationsPage
import profile.{UserProfilePreviewPage, EditUserProfilePage}
import org.apache.wicket.Component

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
      add(AuthorizeUser(new BookmarkablePageLink("conversationsLink", classOf[UserConversationsPage])))
      add(AuthorizeUser(new BookmarkablePageLink("contactsLink", classOf[ContactsPage])))
      add(AuthorizeUser(new BookmarkablePageLink("invitationsSentLink", classOf[InvitationsSentPage])))
      add(AuthorizeUser(new BookmarkablePageLink("invitationsReceivedLink", classOf[InvitationsReceivedPage])))
      add(AuthorizeUser(new BookmarkablePageLink("editProfileLink", classOf[EditUserProfilePage])))
      add(AuthorizeUser(UserProfilePreviewPage.getLink(session.user)))
    })
  } else {
    add(new Fragment("userInfo", "userNotLoggedIn", this) {
      add(new SignInFormPanel("signInPanel"))
    })
  }

  /*add(new DebugBar("debug"))  */
  /*add(new ApplicationView("applicationView", getApplication))
  add(new SessionView("sessionView",session))
  add(new InspectorDebugPanel("inspectorDebugPanel"))
  add(new SessionSizeDebugPanel("sessionSizeDebugPanel"))
  add(new InspectorBug("inspectorBug",this))*/


  override def onBeforeRender() {
    super.onBeforeRender()
    println(this.getClass + " stateless:" + isPageStateless)
  }

  protected def setSubTitle(title: String) {
    titleLabelModel.setObject("Socnet " + title)
  }
  
  protected def addAndReturn(component:Component):Component = {
    add(component)
    component
  }

}
