package pl.marpiec.socnet.web.page.contacts

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.constant.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.{ContactInvitationDatabase, UserContactsDatabase, UserDatabase}
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.service.usercontacts.UserContactsCommand
import pl.marpiec.socnet.service.contactinvitation.ContactInvitationCommand

/**
 * @author Marcin Pieciukiewicz
 */

class InvitationsSentPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var contactInvitationDatabase: ContactInvitationDatabase = _
  @SpringBean private var contactInvitationCommand: ContactInvitationCommand = _
  @SpringBean private var userDatabase: UserDatabase = _

  val invitations = contactInvitationDatabase.getSentInvitations(session.userId)

  val currentUserId = session.userId

  add(new RepeatingView("invitation") {

    invitations.foreach(invitation => {

      val userOption = userDatabase.getUserById(invitation.receiverUserId)

      if (userOption.isEmpty) {
        throw new IllegalStateException("User invitation with incorrect userId")
      }

      val user = userOption.get

      add(new AbstractItem(newChildId()) {
        setOutputMarkupId(true)
        add(UserProfilePreviewPage.getLink("profileLink", user).add(new Label("userName", user.fullName)))

        if (invitation.isAccepted) {
          add(new Fragment("invitationStatus", "accepted", InvitationsSentPage.this))
        } else {
          add(new Fragment("invitationStatus", "waitingForAcceptance", InvitationsSentPage.this))
        }

        add(new AjaxLink("cancelLink") {
          def onClick(target: AjaxRequestTarget) {

            contactInvitationCommand.cancelInvitation(currentUserId, invitation.id, invitation.version)

            val parent = getParent
            parent.setVisible(false)
            target.add(parent)

          }
        })

      })
    })
  })
}
