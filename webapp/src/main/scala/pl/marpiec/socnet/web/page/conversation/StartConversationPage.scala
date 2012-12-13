package pl.marpiec.socnet.web.page.conversation

import conversationPage.{PageWithInvitingPeopleSupport, InvitePeoplePopupPanel}
import model.StartConversationFormModel
import pl.marpiec.util.{IdProtectionUtil, UID}
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.UserDatabase
import pl.marpiec.socnet.service.conversation.ConversationCommand
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.web.component.wicket.form.{OneButtonAjaxForm, StandardAjaxSecureForm}
import org.apache.wicket.model.{PropertyModel, CompoundPropertyModel}
import pl.marpiec.socnet.web.component.editor.BBCodeEditor
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.model.User
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.constant.SocnetRoles
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.component.user.UserSummaryPreviewPanel
import pl.marpiec.socnet.web.page.HomePage
import org.apache.wicket.markup.html.WebMarkupContainer

object StartConversationPage {
  val USER_ID_PARAM = "userId"

  def getLink(componentId: String, userId: UID): BookmarkablePageLink[_] = {
    new BookmarkablePageLink(componentId, classOf[StartConversationPage],
      new PageParameters().add(StartConversationPage.USER_ID_PARAM, IdProtectionUtil.encrypt(userId)))
  }
}

class StartConversationPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) with PageWithInvitingPeopleSupport {


  //dependencies
  @SpringBean private var userDatabase: UserDatabase = _
  @SpringBean private var conversationCommand: ConversationCommand = _
  @SpringBean private var uidGenerator: UidGenerator = _

  //get data

  val userOption = getUserOptionOrThrow404
  var invitedUsers = userDatabase.getUsersByIds(createInvitedUsersIdsList(userOption))

  //build schema

  add(new InvitePeoplePopupPanel("invitePeoplePopupPanel", this))

  addOrReplaceInvitedUsersPanel



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

        conversationCommand.createConversation(session.userId, formModel.conversationTitle,
          session.userId :: invitedUsers.map(_.id), conversationId,
          formModel.messageText, messageId)

        setResponsePage(classOf[ConversationPage], ConversationPage.getParametersForLink(conversationId))

      } else {
        if (StringUtils.isBlank(formModel.messageText)) {
          formModel.warningMessage = "Wiadomosc nie moze byc pusta"
        } else {
          formModel.conversationTitle = "Tytuł wiadomości nie może być pusty"
        }
        target.add(warningMessageLabel)
      }
    }

    def onSecureCancel(target: AjaxRequestTarget, formModel: StartConversationFormModel) {
      setResponsePage(classOf[HomePage])
    }
  })

  private def addOrReplaceInvitedUsersPanel: WebMarkupContainer = {
    val invitedUsersPanel = new WebMarkupContainer("invitedUsersPanel") {
      setOutputMarkupId(true)
      add(new RepeatingView("invitedUser") {
        invitedUsers.foreach(user => {
          add(new AbstractItem(newChildId()) {
            setOutputMarkupId(true)
            val thisItem = this
            add(new UserSummaryPreviewPanel("userSummaryPreview", user))
            add(new OneButtonAjaxForm("removeInvitedUserButton", "Usuń z listy zaproszonych", target => {
              invitedUsers = invitedUsers.filterNot(_.id == user.id)
              thisItem.setVisible(false)
              target.add(thisItem)
            }))
          })
        })
      })
    }
    addOrReplace(invitedUsersPanel)
    invitedUsersPanel
  }

  override def userIsInvitedOrParticipating(userId: UID) = {
    invitedUsers.find(_.id == userId).isDefined
  }

  override def updateConversationData(target: AjaxRequestTarget) {
    target.add(addOrReplaceInvitedUsersPanel)
  }

  private def getUserOptionOrThrow404: Option[User] = {
    val userIdParam = parameters.get(StartConversationPage.USER_ID_PARAM).toString;
    if (StringUtils.isNotBlank(userIdParam)) {
      val userId = IdProtectionUtil.decrypt(userIdParam)
      val userOption = userDatabase.getUserById(userId)
      if (userOption.isEmpty) {
        throw new AbortWithHttpErrorCodeException(404);
      }
      userOption
    } else {
      None
    }
  }

  private def createInvitedUsersIdsList(userOption: Option[User]): List[UID] = {
    if (userOption.isDefined) {
      userOption.get.id :: Nil
    } else {
      Nil
    }
  }

  override def newInvitedPeopleAdded(users: List[User]) {
    users.foreach(user => {
      if (invitedUsers.find(_.id == user.id).isEmpty) {
        invitedUsers ::= user
      }
    })
  }
}
