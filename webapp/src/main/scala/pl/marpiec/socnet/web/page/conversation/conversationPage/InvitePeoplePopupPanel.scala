package pl.marpiec.socnet.web.page.conversation.conversationPage

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.AttributeModifier
import org.apache.wicket.markup.html.link.AbstractLink
import org.apache.wicket.markup.html.form.HiddenField
import pl.marpiec.socnet.web.component.user.UserSummaryPreviewNoLinkPanel
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.{UserDatabase, UserContactsDatabase}
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import pl.marpiec.socnet.web.page.conversation.model.InviteUsersFormModel
import org.apache.wicket.model.{CompoundPropertyModel, Model}
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.util.UID
import pl.marpiec.socnet.web.page.conversation.{ConversationsListenerCenter, ConversationPage}
import pl.marpiec.socnet.service.conversation.ConversationCommand
import pl.marpiec.socnet.model.Conversation

/**
 * @author Marcin Pieciukiewicz
 */

class InvitePeoplePopupPanel(id: String, parentPage: ConversationPage, conversation: Conversation) extends Panel(id) {

  @SpringBean private var conversationCommand: ConversationCommand = _
  @SpringBean private var userContactsDatabase: UserContactsDatabase = _
  @SpringBean private var userDatabase: UserDatabase = _

  val currentUserId = getSession.asInstanceOf[SocnetSession].userId

  val userContactsIds = userContactsDatabase.getUserContactsByUserId(currentUserId).getOrElse(
    throw new IllegalStateException("User contacts not created for user " + currentUserId)
  )

  val userContactsMap = loadUsersAndCreateUserContactsMap(userContactsIds.contactsIds)

  add(new RepeatingView("contact") {
    for ((id, user) <- userContactsMap) {

      add(new AbstractItem(newChildId()) {
        val disabled = parentPage.userIsInvitedOrParticipating(user.id)
        if (disabled) {
          add(new AttributeModifier("class", "contact disabled"))
        } else {
          add(new AttributeModifier("class", "contact"))
        }
        add(new AbstractLink("link") {
          if (!disabled) {
            add(new AttributeModifier("href", "#"))
          }
          add(new HiddenField[String]("k", new Model(id.toString)) {
            override def getInputName = ""
          })
          add(new UserSummaryPreviewNoLinkPanel("userSummaryPreview", user))
        })


      })
    }
  })


  add(new StandardAjaxSecureForm[InviteUsersFormModel]("inviteUsersForm") {
    def initialize = {
      standardCancelButton = false
      setModel(new CompoundPropertyModel[InviteUsersFormModel](new InviteUsersFormModel))
    }

    def buildSchema = {
      add(new HiddenField[String]("users"))
    }

    def onSecureSubmit(target: AjaxRequestTarget, formModel: InviteUsersFormModel) {

      val usersIds: List[UID] = getUserContactsIdsFromUsersMap(formModel.parseUsers)

      if (usersIds.nonEmpty) {
        conversationCommand.addParticipants(currentUserId, conversation.id, conversation.version, usersIds)
      }

      ConversationsListenerCenter.conversationChanged(conversation.id)
      parentPage.updateConversationData(target)

      target.appendJavaScript("hideInviteNewPersonPopup();")

    }

    def onSecureCancel(target: AjaxRequestTarget, formModel: InviteUsersFormModel) {} //handled by JavaScript
  })


  private def loadUsersAndCreateUserContactsMap(userContactsIds: Set[UID]) = {
    val userContacts = userDatabase.getUsersByIds(userContactsIds.toList)
    var counter = 0
    userContacts.map(user => {
      val mapEntry = (counter, user)
      counter += 1
      mapEntry
    }).toMap
  }

  private def getUserContactsIdsFromUsersMap(users: List[Int]): List[UID] = {
    var usersIds = List[UID]()

    users.foreach(userIdentifier => {
      val userOption = userContactsMap.get(userIdentifier)
      if (userOption.isDefined) {
        usersIds ::= userOption.get.id
      }
    })
    usersIds
  }


}
