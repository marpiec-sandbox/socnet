package pl.marpiec.socnet.web.page.contacts

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.UserContactsDatabase
import pl.marpiec.socnet.readdatabase.UserDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.service.usercontacts.UserContactsCommand

/**
 * @author Marcin Pieciukiewicz
 */

class InvitationsSentPage extends SecureWebPage(SocnetRoles.USER) {
  @SpringBean
  var userContactsCommand: UserContactsCommand = _
  @SpringBean
  var userContactsDatabase: UserContactsDatabase = _
  @SpringBean
  var userDatabase: UserDatabase = _

  val userContacts = userContactsDatabase.getUserContactsByUserId(session.userId).get
  val invitations = userContacts.notRemovedInvitationsSent

  val currentUserId = session.userId

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
          add(new Fragment("invitationStatus", "accepted", InvitationsSentPage.this))
        } else {
          add(new Fragment("invitationStatus", "waitingForAcceptance", InvitationsSentPage.this))
        }

        add(new AjaxLink("removeLink") {
          def onClick(target: AjaxRequestTarget) {

            userContactsCommand.removeSentInvitation(currentUserId, userContacts.id, invitation.id)

            val parent = getParent
            parent.setVisible(false)
            target.add(parent)

          }
        })

      })
    })
  })
}
