package pl.marpiec.socnet.web.page.contacts

import invitationsPage.{SentInvitationsPanel, ReceivedInvitationsPanel}
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.constant.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.contactinvitation.ContactInvitationCommand
import pl.marpiec.socnet.service.useractionsinfo.UserActionsInfoCommand
import pl.marpiec.socnet.readdatabase.{UserDatabase, UserActionsInfoDatabase, ContactInvitationDatabase}
import org.joda.time.LocalDateTime
import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class InvitationsPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var contactInvitationDatabase: ContactInvitationDatabase = _
  @SpringBean private var userActionsInfoDatabase: UserActionsInfoDatabase = _
  @SpringBean private var userActionsInfoCommand: UserActionsInfoCommand = _
  @SpringBean private var userDatabase: UserDatabase = _

  val currentUserId = session.userId

  val userActionsInfo = userActionsInfoDatabase.getUserActionsInfoByUserId(session.userId).
    getOrElse(throw new IllegalStateException("No Actions Info defined for user, userId=" + session.userId))

  userActionsInfoCommand.changeContactInvitationsReadTime(session.userId, userActionsInfo.id, new LocalDateTime())

  val (sentInvitations, receivedInvitations) = contactInvitationDatabase.getAllInvitations(session.userId).partition(_.senderUserId == currentUserId)

  val otherUserIds = sentInvitations.map(_.receiverUserId) ::: receivedInvitations.map(_.senderUserId)

  val usersMap:Map[UID, User] = userDatabase.getUsersByIds(otherUserIds).map(user => (user.id, user)).toMap

  add(new ReceivedInvitationsPanel("receivedInvitationsPanel", receivedInvitations, usersMap))
  add(new SentInvitationsPanel("sentInvitationsPanel", sentInvitations, usersMap))



}
