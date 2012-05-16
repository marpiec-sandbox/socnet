package pl.marpiec.socnet.web.page.messages

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import socnet.readdatabase.UserContactsDatabase
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.page.ArticlePage
import org.apache.wicket.markup.html.link.BookmarkablePageLink

/**
 * @author Marcin Pieciukiewicz
 */

class MessagesPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean
  var userContactsDatabase: UserContactsDatabase = _

  val userContacts = userContactsDatabase.getUserContactsByUserId(session.userId).get

  val invitations = userContacts.invitationsReceived

  add(new RepeatingView("invitation") {
    
    invitations.foreach(invitation => {

      add(new AbstractItem(newChildId()) {

        add(new Label("invitationSender", invitation.possibleContactUserId.toString))
        add(new Label("invitationMessage", invitation.message))

        add(new BookmarkablePageLink("messagePreviewLink", classOf[MessagePreviewPage],
            new PageParameters().add(MessagePreviewPage.MESSAGE_ID_PARAM, invitation.id)))
      })
      
    })
    
    
  })


}
