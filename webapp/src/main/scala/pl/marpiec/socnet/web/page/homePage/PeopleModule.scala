package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import people.PeopleDashboardPanel
import pl.marpiec.socnet.web.authorization.AuthorizeUser
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.{UserDatabase, UserContactsDatabase}
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.repeater.RepeatingView._
import pl.marpiec.socnet.web.component.user.UserSummaryPreviewPanel

/**
 * @author Marcin Pieciukiewicz
 */

class PeopleModule(id:String) extends Panel(id) {

  @SpringBean var userContactsDatabase: UserContactsDatabase = _
  @SpringBean var userDatabase: UserDatabase = _

  if(getSession.asInstanceOf[SocnetSession].isAuthenticated) {
    val CONTACTS_COUNT_TO_DISPLAY = 6

    val userContacts = userContactsDatabase.getUserContactsByUserId(getSession.asInstanceOf[SocnetSession].userId).get
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

  add(AuthorizeUser(new PeopleDashboardPanel("peopleDashboard")))


}
