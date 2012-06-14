package pl.marpiec.socnet.web.page.conversation

import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import socnet.service.conversation.ConversationCommand
import socnet.readdatabase.ConversationDatabase
import pl.marpiec.socnet.model.User
import pl.marpiec.util.UID
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */


object ConversationPage {
  val CONVERSATION_ID_PARAM = "conversationId"
}


class ConversationPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean
  private var conversationCommand: ConversationCommand = _

  @SpringBean
  private var conversationDatabase: ConversationDatabase = _


  val conversation = getConversationOrThrow404



  private def getConversationOrThrow404:Conversation = {
    val conversationId = UID.parseOrZero(parameters.get(ConversationPage.CONVERSATION_ID_PARAM).toString)

    val conversationOption = conversationDatabase.getConversationById(conversationId)

    if (conversationOption.isEmpty || !conversationOption.get.participantsUserIds.contains(session.userId())) {
      throw new AbortWithHttpErrorCodeException(404);
    }
    conversationOption.get
  }
}
