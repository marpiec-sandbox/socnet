package pl.marpiec.socnet.web.page.conversation

import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.socnet.web.authorization.SecureWebPage
import org.apache.wicket.spring.injection.annot.SpringBean
import socnet.readdatabase.ConversationDatabase
import socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

class UserConversations extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean
  private var conversationDatabase: ConversationDatabase = _

  val userConversations:List[Conversation] = conversationDatabase.getConversationsByParticipantUserId(session.userId())



}
