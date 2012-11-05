package pl.marpiec.socnet.web.page.contacts

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.constant.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import pl.marpiec.socnet.web.component.contacts.model.InviteUserFormModel
import org.apache.wicket.markup.html.WebMarkupContainer
import pl.marpiec.socnet.web.wicket.SecureFormModel
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.model.ContactInvitation
import pl.marpiec.socnet.service.contactinvitation.ContactInvitationCommand
import pl.marpiec.socnet.service.useractionsinfo.UserActionsInfoCommand
import pl.marpiec.socnet.readdatabase.{UserActionsInfoDatabase, ContactInvitationDatabase, UserDatabase}
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class InvitationsReceivedPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var contactInvitationDatabase: ContactInvitationDatabase = _
  @SpringBean private var contactInvitationCommand: ContactInvitationCommand = _
  @SpringBean private var userActionsInfoDatabase: UserActionsInfoDatabase = _
  @SpringBean private var userActionsInfoCommand: UserActionsInfoCommand = _
  @SpringBean private var userDatabase: UserDatabase = _

  val userActionsInfo = userActionsInfoDatabase.getUserActionsInfoByUserId(session.userId).
    getOrElse(throw new IllegalStateException("No Actions Info defined for user, userId=" + session.userId))

  userActionsInfoCommand.changeContactInvitationsReadTime(session.userId, userActionsInfo.id, new LocalDateTime())

  val invitations = contactInvitationDatabase.getReceivedInvitations(session.userId)

  val currentUserId = session.userId

  add(new RepeatingView("invitation") {

    setOutputMarkupId(true)

    invitations.foreach(invitation => {

      val userOption = userDatabase.getUserById(invitation.senderUserId)

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
          addOrReplace(new Fragment("invitationStatus", "accepted", InvitationsReceivedPage.this))
        }

        def addOrReplaceDeclined {
          addOrReplace(new Fragment("invitationStatus", "declined", InvitationsReceivedPage.this))
        }

        def addOrReplaceWaitingForAcceptance(invitation: ContactInvitation, parent: WebMarkupContainer) {
          addOrReplace(new Fragment("invitationStatus", "waitingForAcceptance", InvitationsReceivedPage.this) {

            add(new StandardAjaxSecureForm[SecureFormModel]("inviteForm") {
              def initialize = {
                setModel(new CompoundPropertyModel[SecureFormModel](new InviteUserFormModel))
              }

              def buildSchema = {} //do nothing

              def onSecureSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {
                contactInvitationCommand.acceptInvitation(session.userId, invitation.id, invitation.version, invitation.senderUserId, invitation.receiverUserId)
                addOrReplaceAccepted
                target.add(parent)
              }

              def onSecureCancel(target: AjaxRequestTarget, formModel: SecureFormModel) {
                contactInvitationCommand.declineInvitation(session.userId, invitation.id, invitation.version)
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
