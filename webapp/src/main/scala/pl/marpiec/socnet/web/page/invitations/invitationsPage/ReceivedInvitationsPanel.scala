package pl.marpiec.socnet.web.page.invitations.invitationsPage

import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
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

class ReceivedInvitationsPanel(id: String, invitations: List[ContactInvitation], usersMap: Map[UID, User]) extends Panel(id) {

  @SpringBean private var contactInvitationCommand: ContactInvitationCommand = _

  val currentUserId = getSession.asInstanceOf[SocnetSession].userId

  setVisible(invitations.nonEmpty)

  add(new RepeatingView("invitation") {

    setOutputMarkupId(true)
    setOutputMarkupPlaceholderTag(true)

    invitations.foreach(invitation => {

      val user = usersMap.getOrElse(invitation.senderUserId, throw new IllegalStateException("User invitation with incorrect userId"))

      add(new AbstractItem(newChildId()) {

        val thisElement = this

        setOutputMarkupId(true)

        add(UserProfilePreviewPage.getLink("profileLink", user).add(new Label("userName", user.fullName)))
        add(new Label("invitationMessage", invitation.message))

        add(new StandardAjaxSecureForm[SecureFormModel]("inviteForm") {
          def initialize {
            setModel(new CompoundPropertyModel[SecureFormModel](new InviteUserFormModel))
          }

          def buildSchema {} //do nothing

          def onSecureSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {
            contactInvitationCommand.acceptInvitation(currentUserId, invitation.id, invitation.version, invitation.senderUserId, invitation.receiverUserId)
            target.appendJavaScript("hideRemovedInvitation('"+thisElement.getMarkupId+"');")
          }

          def onSecureCancel(target: AjaxRequestTarget, formModel: SecureFormModel) {
            contactInvitationCommand.declineInvitation(currentUserId, invitation.id, invitation.version)
            target.appendJavaScript("hideRemovedInvitation('"+thisElement.getMarkupId+"');")
          }
        })

      })
    })
  })
}
