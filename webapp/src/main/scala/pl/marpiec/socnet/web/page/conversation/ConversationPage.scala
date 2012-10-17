package pl.marpiec.socnet.web.page.conversation

import model.ReplyConversationFormModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.constant.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.conversation.ConversationCommand
import pl.marpiec.util.UID
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import pl.marpiec.socnet.readdatabase.UserDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.component.conversation.MessagePreviewPanel
import pl.marpiec.socnet.web.component.editor.BBCodeEditor
import org.apache.wicket.model.{PropertyModel, CompoundPropertyModel}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.commons.lang.StringUtils
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.cqrs.exception.ConcurrentAggregateModificationException
import org.apache.wicket.markup.html.WebMarkupContainer
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.web.page.profile.UserProfilePreviewPage
import pl.marpiec.socnet.readdatabase.{ConversationInfoDatabase, ConversationDatabase}
import pl.marpiec.socnet.model.Conversation
import pl.marpiec.socnet.web.component.wicket.form.{OneButtonAjaxForm, StandardAjaxSecureForm}

/**
 * @author Marcin Pieciukiewicz
 */


object ConversationPage {
  val CONVERSATION_ID_PARAM = "conversationId"
}


class ConversationPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  // dependencies

  @SpringBean private var conversationCommand: ConversationCommand = _
  @SpringBean private var conversationDatabase: ConversationDatabase = _
  @SpringBean private var conversationInfoDatabase: ConversationInfoDatabase = _
  @SpringBean private var userDatabase: UserDatabase = _
  @SpringBean private var uidGenerator: UidGenerator = _


  // load data
  var conversation = getConversationOrThrow404

  var participants = userDatabase.getUsersByIds(conversation.participantsUserIds)

  val conversationInfo = conversationInfoDatabase.getConversationInfo(session.userId, conversation.id).
    getOrElse(throw new IllegalStateException("User has no defined info for conversation"))

  conversationCommand.userHasReadConversation(session.userId, conversationInfo.id, conversationInfo.version)
  conversationInfo.version = conversationInfo.version + 1


  // build schema

  var conversationPreviewPanel = addAndReturn(createConversationPreview)


  // reply conversation form
  add(new StandardAjaxSecureForm[ReplyConversationFormModel]("replyForm") {

    var model: ReplyConversationFormModel = _

    def initialize = {
      model = new ReplyConversationFormModel
      setModel(new CompoundPropertyModel[ReplyConversationFormModel](model))
      standardCancelButton = false
    }

    def buildSchema = {
      add(new BBCodeEditor("bbCodeEditor", new PropertyModel[String](model, "messageText")))
    }

    def onSecureSubmit(target: AjaxRequestTarget, formModel: ReplyConversationFormModel) {
      try {

        if (StringUtils.isNotBlank(formModel.messageText)) {

          val messageId = uidGenerator.nextUid
          conversationCommand.createMessage(session.userId, conversation.id, conversation.version, formModel.messageText, messageId)

          formModel.warningMessage = ""
          formModel.messageText = ""

          reloadConversationFromDB

          target.add(conversationPreviewPanel)
          target.add(this.warningMessageLabel)
          target.appendJavaScript("clearBBEditor()")

        } else {

          formModel.warningMessage = "Wiadomosc nie moze byc pusta"
          target.add(warningMessageLabel)
        }
      } catch {
        case e: ConcurrentAggregateModificationException => {
          formModel.warningMessage = "Conversation has changed"

          reloadConversationFromDB

          target.add(conversationPreviewPanel)
          target.add(this.warningMessageLabel)
        }
      }
    }

    def onSecureCancel(target: AjaxRequestTarget, formModel: ReplyConversationFormModel) {
      //ignore, javascript will handle this
    }
  })


  private def reloadConversationFromDB {
    conversation = conversationDatabase.getConversationById(conversation.id).get
    participants = userDatabase.getUsersByIds(conversation.participantsUserIds)
    conversationPreviewPanel = createConversationPreview
    ConversationPage.this.addOrReplace(conversationPreviewPanel)
  }


  private def createConversationPreview: WebMarkupContainer = {
    new WebMarkupContainer("conversationPreview") {

      setOutputMarkupId(true)

      add(new Label("conversationTitle", conversation.title))

      add(new RepeatingView("participant") {
        participants.foreach(user => {
          add(new AbstractItem(newChildId()) {
            add(UserProfilePreviewPage.getLink(user).add(new Label("userName", user.fullName)))
          })
        })
      })

      add(new RepeatingView("message") {
        conversation.messages.reverse.foreach(message => {
          add(new AbstractItem(newChildId()) {
            add(new MessagePreviewPanel("messagePreview", message, findUserInParticipants(message.authorUserId)))
          })
        })
      })

      add(new OneButtonAjaxForm("exitConversationButton", "Opusc rozmowe", (target: AjaxRequestTarget) => {
        conversationCommand.exitConversation(session.userId, conversationInfo.id, conversationInfo.version)
        setResponsePage(classOf[ConversationPage], new PageParameters().add(ConversationPage.CONVERSATION_ID_PARAM, conversation.id))
      }).setVisible(conversationInfo.participating && !conversationInfo.deleted))

      add(new OneButtonAjaxForm("enterConversationButton", "Dolacz do rozmowy", (target: AjaxRequestTarget) => {
        conversationCommand.enterConversation(session.userId, conversationInfo.id, conversationInfo.version)
        setResponsePage(classOf[ConversationPage], new PageParameters().add(ConversationPage.CONVERSATION_ID_PARAM, conversation.id))
      }).setVisible(!conversationInfo.participating && !conversationInfo.deleted))

      add(new OneButtonAjaxForm("deleteConversationButton", "Usun rozmowe", (target: AjaxRequestTarget) => {
        conversationCommand.removeConversation(session.userId, conversationInfo.id, conversationInfo.version)
        setResponsePage(classOf[UserConversationsPage])
      }).setVisible(!conversationInfo.deleted))

    }
  }

  private def getConversationOrThrow404: Conversation = {
    val conversationId = UID.parseOrZero(parameters.get(ConversationPage.CONVERSATION_ID_PARAM).toString)

    val conversationOption = conversationDatabase.getConversationById(conversationId)

    if (conversationOption.isEmpty || !conversationOption.get.participantsUserIds.contains(session.userId)) {
      throw new AbortWithHttpErrorCodeException(404);
    }
    conversationOption.get
  }

  private def findUserInParticipants(userId: UID): User = {
    participants.find(user => {
      user.id == userId
    }).get
  }
}
