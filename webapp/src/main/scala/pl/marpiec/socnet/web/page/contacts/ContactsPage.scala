package pl.marpiec.socnet.web.page.contacts

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.UserContactsDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.readdatabase.UserDatabase
import pl.marpiec.socnet.web.component.user.UserSummaryPreviewPanel

/**
 * @author Marcin Pieciukiewicz
 */

class ContactsPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean var userContactsDatabase: UserContactsDatabase = _
  @SpringBean var userDatabase: UserDatabase = _

  val userContacts = userContactsDatabase.getUserContactsByUserId(session.userId).get
  val contacts = userContacts.contacts

  add(new RepeatingView("contact") {

    contacts.foreach(contact => {

      val userOption = userDatabase.getUserById(contact.contactUserId)

      if (userOption.isEmpty) {
        throw new IllegalStateException("User contact with incorrect userId")
      }

      val user = userOption.get

      add(new AbstractItem(newChildId()) {
        add(new UserSummaryPreviewPanel("userSummaryPreview", user))
      })
    })
  })

}