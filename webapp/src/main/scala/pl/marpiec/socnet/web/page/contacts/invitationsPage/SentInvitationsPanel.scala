package pl.marpiec.socnet.web.page.contacts.invitationsPage

import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{User, ContactInvitation}
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.contactinvitation.ContactInvitationCommand
import pl.marpiec.socnet.web.application.SocnetSession

/**
 * @author Marcin Pieciukiewicz
 */

class SentInvitationsPanel(id: String, invitations: List[ContactInvitation], usersMap:Map[UID, User]) extends Panel(id) {

  @SpringBean private var contactInvitationCommand: ContactInvitationCommand = _

  val currentUserId = getSession.asInstanceOf[SocnetSession].userId

  add(new RepeatingView("invitation") {

    invitations.foreach(invitation => {

      val userOption = usersMap.get(invitation.receiverUserId)

      if (userOption.isEmpty) {
        throw new IllegalStateException("User invitation with incorrect userId")
      }

      val user = userOption.get

      add(new AbstractItem(newChildId()) {
        setOutputMarkupId(true)
        add(UserProfilePreviewPage.getLink("profileLink", user).add(new Label("userName", user.fullName)))

        if (invitation.isAccepted) {
          add(new Fragment("invitationStatus", "accepted", SentInvitationsPanel.this))
        } else {
          add(new Fragment("invitationStatus", "waitingForAcceptance", SentInvitationsPanel.this))
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
