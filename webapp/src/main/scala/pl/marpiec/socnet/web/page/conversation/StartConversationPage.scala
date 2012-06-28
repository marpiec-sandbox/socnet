package pl.marpiec.socnet.web.page.conversation

import model.StartConversationFormModel
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.UserDatabase
import pl.marpiec.util.UID
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import pl.marpiec.socnet.web.component.editor.BBCodeEditor
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.{PropertyModel, CompoundPropertyModel}
import org.apache.wicket.markup.html.form.TextField
import pl.marpiec.socnet.service.conversation.ConversationCommand
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.model.User

/**
 * @author Marcin Pieciukiewicz
 */

object StartConversationPage {
  val USER_ID_PARAM = "userId"
}

class StartConversationPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  //dependencies
  @SpringBean private var userDatabase: UserDatabase = _
  @SpringBean private var conversationCommand: ConversationCommand = _
  @SpringBean private var uidGenerator: UidGenerator = _


  //get data
  val user = getUserOrThrow404

  //build schema

  add(new Label("contactFullName", user.fullName))

  add(new StandardAjaxSecureForm[StartConversationFormModel]("startConversationForm") {

    var model: StartConversationFormModel = _

    def initialize = {
      model = new StartConversationFormModel
      setModel(new CompoundPropertyModel[StartConversationFormModel](model))
    }

    def buildSchema = {
      add(new BBCodeEditor("bbCodeEditor", new PropertyModel[String](model, "messageText")))
      add(new TextField[String]("conversationTitle"))
    }

    def onSecureSubmit(target: AjaxRequestTarget, formModel: StartConversationFormModel) {

      if (StringUtils.isNotBlank(formModel.conversationTitle) &&
        StringUtils.isNotBlank(formModel.messageText)) {

        val conversationId = uidGenerator.nextUid
        val messageId = uidGenerator.nextUid

        conversationCommand.createConversation(session.userId(), formModel.conversationTitle, createParticipantsList, conversationId,
          formModel.messageText, messageId)

        setResponsePage(classOf[ConversationPage], new PageParameters().add(ConversationPage.CONVERSATION_ID_PARAM, conversationId))

      } else {
        if (StringUtils.isBlank(formModel.messageText)) {
          formModel.warningMessage = "Wiadomosc nie moze byc pusta"
        } else {
          formModel.conversationTitle = "Tytu? wiadomo?ci nie mo?e by? pusty"
        }
        target.add(warningMessageLabel)
      }
    }

    def onSecureCancel(target: AjaxRequestTarget, formModel: StartConversationFormModel) {
      setResponsePage(classOf[UserProfilePreviewPage], UserProfilePreviewPage.getParametersForLink(user))
    }
  })


  private def getUserOrThrow404: User = {
    val userId = UID.parseOrZero(parameters.get(StartConversationPage.USER_ID_PARAM).toString)

    val userOption = userDatabase.getUserById(userId)

    if (userOption.isEmpty) {
      throw new AbortWithHttpErrorCodeException(404);
    }
    userOption.get
  }


  private def createParticipantsList: List[UID] = {
    session.userId() :: user.id :: Nil
  }


}
