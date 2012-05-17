package pl.marpiec.socnet.web.page.contacts

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import socnet.readdatabase.UserContactsDatabase
import pl.marpiec.socnet.readdatabase.UserDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.repeater.RepeatingView._
import pl.marpiec.socnet.web.page.UserProfilePreviewPage
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class InvitationsReceivedPage extends SecureWebPage(SocnetRoles.USER) {
  @SpringBean
  var userContactsDatabase: UserContactsDatabase = _
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
        add(UserProfilePreviewPage.getLink(user).add(new Label("userName", user.fullName)))
      })
    })
  })
}
