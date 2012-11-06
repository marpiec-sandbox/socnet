package pl.marpiec.socnet.web.page.invitations.invitationsPage

import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
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

class SentInvitationsPanel(id: String, invitations: List[ContactInvitation], usersMap: Map[UID, User]) extends Panel(id) {

  @SpringBean private var contactInvitationCommand: ContactInvitationCommand = _

  val currentUserId = getSession.asInstanceOf[SocnetSession].userId
  var remainingInvitations = invitations.size

  setVisible(invitations.nonEmpty)

  add(new RepeatingView("invitation") {

    invitations.foreach(invitation => {

      val user = usersMap.getOrElse(invitation.receiverUserId, throw new IllegalStateException("User invitation with incorrect userId"))

      add(new AbstractItem(newChildId()) {

        val thisElement = this

        setOutputMarkupId(true)
        setOutputMarkupPlaceholderTag(true)

        add(UserProfilePreviewPage.getLink("profileLink", user).add(new Label("userName", user.fullName)))

        add(new AjaxLink("cancelLink") {
          def onClick(target: AjaxRequestTarget) {

            contactInvitationCommand.cancelInvitation(currentUserId, invitation.id, invitation.version)

            thisElement.setVisible(false)
            target.add(thisElement)
            
          }
        })

      })
    })
  })
}
