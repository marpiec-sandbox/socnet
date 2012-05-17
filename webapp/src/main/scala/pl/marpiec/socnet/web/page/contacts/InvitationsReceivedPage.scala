package pl.marpiec.socnet.web.page.contacts

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import socnet.readdatabase.UserContactsDatabase
import pl.marpiec.socnet.readdatabase.UserDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import pl.marpiec.socnet.web.component.contacts.model.InviteUserFormModel
import socnet.service.usercontacts.UserContactsCommand
import socnet.model.usercontacts.Invitation
import org.apache.wicket.markup.html.WebMarkupContainer
import pl.marpiec.socnet.web.wicket.SecureFormModel
import org.apache.wicket.model.CompoundPropertyModel

/**
 * @author Marcin Pieciukiewicz
 */

class InvitationsReceivedPage extends SecureWebPage(SocnetRoles.USER) {
  @SpringBean
  var userContactsDatabase: UserContactsDatabase = _
  @SpringBean
  var userContactsCommand: UserContactsCommand = _
  @SpringBean
  var userDatabase: UserDatabase = _

  val userContacts = userContactsDatabase.getUserContactsByUserId(session.userId).get
  val invitations = userContacts.invitationsReceived


  add(new RepeatingView("invitation") {

    invitations.foreach(invitation => {

      val userOption = userDatabase.getUserById(invitation.possibleContactUserId)

      if (userOption.isEmpty) {
        throw new IllegalStateException("User invitation with incorrect userId")
      }

      val user = userOption.get

      add(new AbstractItem(newChildId()) {

        setOutputMarkupId(true)
        add(UserProfilePreviewPage.getLink(user).add(new Label("userName", user.fullName)))

        if (invitation.accepted) {
          addOrReplaceAccepted
        } else if (invitation.declined) {
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

        def addOrReplaceWaitingForAcceptance(invitation:Invitation, parent: WebMarkupContainer) {
          addOrReplace(new Fragment("invitationStatus", "waitingForAcceptance", InvitationsReceivedPage.this) {

            add(new StandardAjaxSecureForm[SecureFormModel]("inviteForm") {
              def initialize = {
                setModel(new CompoundPropertyModel[SecureFormModel](new InviteUserFormModel))
              }

              def buildSchema = {} //do nothing

              def onSecureSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {
                userContactsCommand.acceptInvitation(session.userId, userContacts.id, invitation.possibleContactUserId, invitation.id)
                addOrReplaceAccepted
                target.add(parent)
              }

              def onSecureCancel(target: AjaxRequestTarget, formModel: SecureFormModel) {
                userContactsCommand.declineInvitation(session.userId, userContacts.id, invitation.possibleContactUserId, invitation.id)
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
