package pl.marpiec.socnet.web.page.profile.userProfilePreviewPage

import pl.marpiec.socnet.model.UserContacts
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.component.user.UserSummaryPreviewPanel
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.UserDatabase

/**
 * @author Marcin Pieciukiewicz
 */

class UserContactsPreviewPanel(id:String, userContacts:UserContacts, loggedInUserContacts:UserContacts) extends Panel(id) {

  val CONTACTS_COUNT_TO_DISPLAY = 10

  @SpringBean
  private var userDatabase: UserDatabase = _

  val contacts = userContacts.contacts

  add(new RepeatingView("contact") {

    contacts.take(CONTACTS_COUNT_TO_DISPLAY).foreach(contact => {

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
