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
import pl.marpiec.socnet.readdatabase.UserDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.repeater.RepeatingView._
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.component.conversation.MessagePreviewPanel

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

  @SpringBean
  private var userDatabase: UserDatabase = _

  val conversation = getConversationOrThrow404

  val participants = userDatabase.getUsersByIds(conversation.participantsUserIds)


  add(new RepeatingView("participant") {

    participants.foreach(user => {

      add(new AbstractItem(newChildId()) {

        add(new Label("userName", user.fullName))

      })
    })
  })

  add(new RepeatingView("message") {
    conversation.messages.foreach(message => {
      add(new AbstractItem(newChildId()) {
        add(new MessagePreviewPanel("messagePreview", message))
      })
    })
  })





  private def getConversationOrThrow404:Conversation = {
    val conversationId = UID.parseOrZero(parameters.get(ConversationPage.CONVERSATION_ID_PARAM).toString)

    val conversationOption = conversationDatabase.getConversationById(conversationId)

    if (conversationOption.isEmpty || !conversationOption.get.participantsUserIds.contains(session.userId())) {
      throw new AbortWithHttpErrorCodeException(404);
    }
    conversationOption.get
  }
}
