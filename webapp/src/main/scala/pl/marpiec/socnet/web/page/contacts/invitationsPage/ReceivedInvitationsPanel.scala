package pl.marpiec.socnet.web.page.contacts.invitationsPage

import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.markup.html.WebMarkupContainer
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import pl.marpiec.socnet.web.wicket.SecureFormModel
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.web.component.contacts.model.InviteUserFormModel
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{User, ContactInvitation}
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.contactinvitation.ContactInvitationCommand

/**
 * @author Marcin Pieciukiewicz
 */

class ReceivedInvitationsPanel(id: String, invitations: List[ContactInvitation], usersMap:Map[UID, User]) extends Panel(id) {

  @SpringBean private var contactInvitationCommand: ContactInvitationCommand = _

  val currentUserId = getSession.asInstanceOf[SocnetSession].userId

  add(new RepeatingView("invitation") {

    setOutputMarkupId(true)

    invitations.foreach(invitation => {

      val userOption = usersMap.get(invitation.senderUserId)

      if (userOption.isEmpty) {
        throw new IllegalStateException("User invitation with incorrect userId")
      }

      val user = userOption.get

      add(new AbstractItem(newChildId()) {

        setOutputMarkupId(true)
        add(UserProfilePreviewPage.getLink("profileLink", user).add(new Label("userName", user.fullName)))

        if (invitation.isAccepted) {
          addOrReplaceAccepted
        } else if (invitation.isDeclined) {
          addOrReplaceDeclined
        } else {
          addOrReplaceWaitingForAcceptance(invitation, this)
        }


        def addOrReplaceAccepted {
          addOrReplace(new Fragment("invitationStatus", "accepted", ReceivedInvitationsPanel.this))
        }

        def addOrReplaceDeclined {
          addOrReplace(new Fragment("invitationStatus", "declined", ReceivedInvitationsPanel.this))
        }

        def addOrReplaceWaitingForAcceptance(invitation: ContactInvitation, parent: WebMarkupContainer) {
          addOrReplace(new Fragment("invitationStatus", "waitingForAcceptance", ReceivedInvitationsPanel.this) {

            add(new StandardAjaxSecureForm[SecureFormModel]("inviteForm") {
              def initialize = {
                setModel(new CompoundPropertyModel[SecureFormModel](new InviteUserFormModel))
              }

              def buildSchema = {} //do nothing

              def onSecureSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {
                contactInvitationCommand.acceptInvitation(currentUserId, invitation.id, invitation.version, invitation.senderUserId, invitation.receiverUserId)
                addOrReplaceAccepted
                target.add(parent)
              }

              def onSecureCancel(target: AjaxRequestTarget, formModel: SecureFormModel) {
                contactInvitationCommand.declineInvitation(currentUserId, invitation.id, invitation.version)
                addOrReplaceDeclined
                target.add(parent)
              }
            })
          })
        }
      })
    })
  })
}
