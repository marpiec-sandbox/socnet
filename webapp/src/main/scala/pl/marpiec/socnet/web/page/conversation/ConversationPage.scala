package pl.marpiec.socnet.web.page.conversation

import model.ReplyConversationFormModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.constant.SocnetRoles
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.conversation.ConversationCommand
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
import pl.marpiec.socnet.web.component.wicket.form.{OneButtonAjaxForm, OneLinkAjaxForm, StandardAjaxSecureForm}
import pl.marpiec.socnet.web.component.user.UserSummaryPreviewPanel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.util.{IdProtectionUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */


object ConversationPage {
  val CONVERSATION_ID_PARAM = "conversationId"

  def getLink(componentId:String, conversationId:UID): BookmarkablePageLink[_] = {
    new BookmarkablePageLink(componentId, classOf[ConversationPage], getParametersForLink(conversationId))
  }

  def getParametersForLink(conversationId:UID): PageParameters = {
    new PageParameters().add(CONVERSATION_ID_PARAM, IdProtectionUtil.encrypt(conversationId))
  }
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
  checkIfUserCanReadConversationOrThrow403



  //TODO optimize loading of all users
  var participants = userDatabase.getUsersByIds(conversation.participantsUserIds)
  var invitedUsers = userDatabase.getUsersByIds(conversation.invitedUserIds)
  var previousUsers = userDatabase.getUsersByIds(conversation.previousUserIds)
  var allUsers = participants ::: invitedUsers ::: previousUsers

  var lastReadTime = updateConversationReadTime()


  // build schema

  add(new Label("conversationTitle", conversation.title))


  var conversationMessagesPreviewPanel = addAndReturn(createConversationMessagesPreviewPanel)

  var conversationInfoPanel = addAndReturn(createConversationInfoPanel)

  add(new WebMarkupContainer("replyButton").setVisible(conversation.userParticipating(session.userId)))

  // reply conversation form
  add(new StandardAjaxSecureForm[ReplyConversationFormModel]("replyForm") {

    var model: ReplyConversationFormModel = _

    def initialize = {
      model = new ReplyConversationFormModel
      setModel(new CompoundPropertyModel[ReplyConversationFormModel](model))
      standardCancelButton = false
      setVisible(conversation.userParticipating(session.userId))
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

          target.add(conversationMessagesPreviewPanel)
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


          target.add(conversationInfoPanel)
          target.add(conversationMessagesPreviewPanel)
          target.add(this.warningMessageLabel)
        }
      }
    }

    def onSecureCancel(target: AjaxRequestTarget, formModel: ReplyConversationFormModel) {
      //ignore, javascript will handle this
    }
  })


  //Methods

  private def reloadConversationFromDB {
    conversation = conversationDatabase.getConversationById(conversation.id).get
    participants = userDatabase.getUsersByIds(conversation.participantsUserIds)
    invitedUsers = userDatabase.getUsersByIds(conversation.invitedUserIds)
    previousUsers = userDatabase.getUsersByIds(conversation.previousUserIds)
    allUsers = participants ::: invitedUsers ::: previousUsers

    lastReadTime = updateConversationReadTime()

    conversationInfoPanel = ConversationPage.this.addOrReplaceAndReturn(createConversationInfoPanel)
    conversationMessagesPreviewPanel = ConversationPage.this.addOrReplaceAndReturn(createConversationMessagesPreviewPanel)
  }

  
  private def createConversationMessagesPreviewPanel = {
    new WebMarkupContainer("messagesContainer") {
      setOutputMarkupId(true)
      add(new RepeatingView("message") {
        conversation.messages.reverse.foreach(message => {
          add(new AbstractItem(newChildId()) {
            add(new MessagePreviewPanel(
              "messagePreview", message,
              findUserInParticipants(message.authorUserId), message.sentTime.isAfter(lastReadTime)))
          })
        })
      })
    }
  }

  private def createConversationInfoPanel = {
    new WebMarkupContainer("conversationInfo") {

      setOutputMarkupId(true)

      add(new WebMarkupContainer("noParticipantsLabel").setVisible(participants.isEmpty))

      add(new RepeatingView("participant") {
        participants.foreach(user => {
          add(new AbstractItem(newChildId()) {
            add(new UserSummaryPreviewPanel("userSummaryPreview", user))
          })
        })
      })

      add(new RepeatingView("invitedUser") {
        invitedUsers.foreach(user => {
          add(new AbstractItem(newChildId()) {
            add(new UserSummaryPreviewPanel("userSummaryPreview", user))
          })
        })
      })

      add(new OneButtonAjaxForm("enterConversationButton", "Dołącz do rozmowy", (target: AjaxRequestTarget) => {
        conversationCommand.enterConversation(session.userId, conversation.id, conversation.version, session.userId)
        setResponsePage(classOf[ConversationPage], ConversationPage.getParametersForLink(conversation.id))
      }).setVisible(conversation.userInvited(session.userId)))

      add(new OneButtonAjaxForm("exitConversationButton", "OK", (target: AjaxRequestTarget) => {
        conversationCommand.exitConversation(session.userId, conversation.id, conversation.version, session.userId)
        setResponsePage(classOf[ConversationPage], ConversationPage.getParametersForLink(conversation.id))
      }).setVisible(conversation.userParticipating(session.userId)))

      add(new OneButtonAjaxForm("deleteConversationButton", "OK", (target: AjaxRequestTarget) => {
        conversationCommand.removeConversationForUser(session.userId, conversation.id, conversation.version, session.userId)
        setResponsePage(classOf[UserConversationsPage])
      }).setVisible(conversation.userInvited(session.userId) || !conversation.userParticipating(session.userId)))

    }
  }


  private def updateConversationReadTime() = {
    val conversationInfo = conversationInfoDatabase.getConversationInfo(session.userId, conversation.id).
      getOrElse(throw new IllegalStateException("User has no defined info for conversation"))
    val previousReadTime = conversationInfo.lastReadTime
    conversationCommand.userHasReadConversation(session.userId, conversationInfo.id, conversationInfo.version)
    conversationInfo.version = conversationInfo.version + 1
    previousReadTime
  }


  private def getConversationOrThrow404: Conversation = {
    val conversationId = IdProtectionUtil.decrypt(parameters.get(ConversationPage.CONVERSATION_ID_PARAM).toString)

    val conversationOption = conversationDatabase.getConversationById(conversationId)

    if (conversationOption.isEmpty) {
      throw new AbortWithHttpErrorCodeException(404);
    }
    conversationOption.get
  }

  private def findUserInParticipants(userId: UID): User = {
    allUsers.find(user => {
      user.id == userId
    }).get
  }

  private def checkIfUserCanReadConversationOrThrow403() {
    if(!conversation.userParticipating(session.userId) && !conversation.userInvited(session.userId)){
      throw new AbortWithHttpErrorCodeException(403);
    }
  }
}
