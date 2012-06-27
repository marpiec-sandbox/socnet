package pl.marpiec.socnet.web.component.contacts

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.util.UID
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class PersonContactLevelPanel(id:String, userId:UID, userContacts:UserContacts, loggedInUserId:UID, loggedInUserContacts:UserContacts) extends Panel(id) {
  
  if (loggedInUserId == userId) {
    add(new Label("contactLevel", "To ty"))
  } else if (loggedInUserContacts.contactByUserId(userId).isDefined) {
    add(new Label("contactLevel", "Twoj kontakt"))
  } else if (usersHaveCommonContact) {
    add(new Label("contactLevel", "Wspolni znajomi"))
  } else {
    add(new Label("contactLevel", "Nieznajomy"))
  }
  
  private def usersHaveCommonContact():Boolean = {

    var haveCommonContacts = false;

    userContacts.contacts.foreach(contact => {
      loggedInUserContacts.contacts.foreach(loggedInUserContact => {
        haveCommonContacts = haveCommonContacts || contact.contactUserId == loggedInUserContact.contactUserId
      })
    })

    haveCommonContacts
  }

}
