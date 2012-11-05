package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.readdatabase.{ContactInvitationDatabase, UserActionsInfoDatabase}
import pl.marpiec.socnet.web.page.contacts.InvitationsReceivedPage
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.text.PolishTextUtil

/**
 * @author Marcin Pieciukiewicz
 */

class SystemMessagesModule(id:String) extends Panel(id) {

  @SpringBean var contactInvitationDatabase: ContactInvitationDatabase = _
  @SpringBean var userActionsInfoDatabase: UserActionsInfoDatabase = _

  if(getSession.asInstanceOf[SocnetSession].isAuthenticated) {
  
    val currentUserId = getSession.asInstanceOf[SocnetSession].userId
    
    val userActionsInfo = userActionsInfoDatabase.getUserActionsInfoByUserId(currentUserId).
      getOrElse(throw new IllegalStateException("User hasn't defined UserActions, userId:"+currentUserId))

    val unreadInvitationsCount = contactInvitationDatabase.getInvitationsCount(currentUserId, userActionsInfo.contactInvitationsReadTimeOption)

    add(new BookmarkablePageLink("invitationsPageLink", classOf[InvitationsReceivedPage]).
      add(new Label("linkLabel", PolishTextUtil.getInvitationsLinkMessage(unreadInvitationsCount)).setVisible(unreadInvitationsCount > 0)))

    setVisible(unreadInvitationsCount > 0)

  } else {
    setVisible(false)
  }
}
