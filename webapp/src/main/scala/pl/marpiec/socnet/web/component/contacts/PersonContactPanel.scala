package pl.marpiec.socnet.web.component.contacts

import model.InviteUserFormModel
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.UserContacts
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.usercontacts.UserContactsCommand
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.Component
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.form.TextArea
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.socnet.model.usercontacts.Invitation
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.conversation.StartConversationPage
import org.apache.wicket.request.mapper.parameter.PageParameters

/**
 * @author Marcin Pieciukiewicz
 */

class PersonContactPanel(id: String, userId: UID, userContacts: UserContacts, loggedInUserContacts: UserContacts) extends Panel(id) {

  @SpringBean private var userContactsCommand: UserContactsCommand = _
  @SpringBean private var uidGenerator: UidGenerator = _

  val contactOption = userContacts.contactByUserId(userId)
  val invitationSentOption = userContacts.invitationSentByUserId(userId)
  val invitationReceivedOption = userContacts.invitationReceivedByUserId(userId)

  val currentUser = getSession.asInstanceOf[SocnetSession].user

  setOutputMarkupId(true)

  add(new BookmarkablePageLink("newConversationLink", classOf[StartConversationPage],
    new PageParameters().add(StartConversationPage.USER_ID_PARAM, userId.uid)))

  if (getSession.asInstanceOf[SocnetSession].userId == userId) {
    add(new Label("contactLevel", "To ty"))
  } else if (loggedInUserContacts.contactByUserId(userId).isDefined) {
    add(new Label("contactLevel", "Twoj kontakt"))
  } else if (usersHaveCommonContact) {
    add(new Label("contactLevel", "Wspolni znajomi"))
  } else {
    add(new Label("contactLevel", "Nieznajomy"))
  }

  private def usersHaveCommonContact(): Boolean = {

    var haveCommonContacts = false;

    userContacts.contacts.foreach(contact => {
      loggedInUserContacts.contacts.foreach(loggedInUserContact => {
        haveCommonContacts = haveCommonContacts || contact.contactUserId == loggedInUserContact.contactUserId
      })
    })

    haveCommonContacts
  }


  if (userContacts.userId == userId) {
    addOrReplaceYourself
  } else if (contactOption.isDefined) {
    addOrReplaceUserIsContact
  } else if (isInvitationReceivedAndWaitingForAcceptance(invitationReceivedOption)) {
    addOrReplaceInvitationReceived
  } else if (invitationSentOption.isDefined) {
    addOrReplaceInvitationSent
  } else {
    addOrReplaceInviteContact
  }


  def addOrReplaceInviteContact {
    addOrReplace(new Fragment("contactStatus", "inviteContact", PersonContactPanel.this) {

      val inviteLink: Component = new AjaxLink("inviteLink") {
        setOutputMarkupPlaceholderTag(true)

        def onClick(target: AjaxRequestTarget) {
          inviteLink.setVisible(false)
          inviteForm.setVisible(true)
          target.add(PersonContactPanel.this)
        }
      }
      add(inviteLink)


      val inviteForm: StandardAjaxSecureForm[InviteUserFormModel] = new StandardAjaxSecureForm[InviteUserFormModel]("inviteForm") {

        def initialize {
          setModel(new CompoundPropertyModel[InviteUserFormModel](new InviteUserFormModel))
          setVisible(false)
        }

        def buildSchema {
          add(new TextArea[String]("inviteMessage"))
        }

        def onSecureSubmit(target: AjaxRequestTarget, formModel: InviteUserFormModel) {
          if (StringUtils.isNotBlank(formModel.inviteMessage)) {

            userContactsCommand.sendInvitation(currentUser.id, userContacts.id,
              userId, formModel.inviteMessage, uidGenerator.nextUid)
            //TODO handle send invitation exceptions

            formModel.inviteMessage = ""
            formModel.warningMessage = ""
            inviteLink.setVisible(true)
            inviteForm.setVisible(false)
            target.add(inviteLink)
            target.add(inviteForm)

            addOrReplaceInvitationSent


          } else {
            formModel.warningMessage = "Wiadomosc nie moze byc pusta"
          }
          target.add(PersonContactPanel.this)
        }

        def onSecureCancel(target: AjaxRequestTarget, formModel: InviteUserFormModel) {
          formModel.inviteMessage = ""
          formModel.warningMessage = ""
          inviteLink.setVisible(true)
          inviteForm.setVisible(false)
          target.add(PersonContactPanel.this)
        }

      }

      add(inviteForm)
    })
  }

  def addOrReplaceInvitationReceived {
    val invitationReceived = invitationReceivedOption.get

    addOrReplace(new Fragment("contactStatus", "invitationReceived", PersonContactPanel.this) {

      add(new StandardAjaxSecureForm[SecureFormModel]("replyForInvitationForm") {
        def initialize {
          setModel(new CompoundPropertyModel[SecureFormModel](new SecureFormModel))
        }

        def buildSchema {} //do nothing

        def onSecureSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {
          userContactsCommand.acceptInvitation(currentUser.id, userContacts.id, userId, invitationReceived.id)

          addOrReplaceUserIsContact

          target.add(PersonContactPanel.this)
        }

        def onSecureCancel(target: AjaxRequestTarget, formModel: SecureFormModel) {
          userContactsCommand.declineInvitation(currentUser.id, userContacts.id, userId, invitationReceived.id)

          addOrReplaceInviteContact

          target.add(PersonContactPanel.this)
        }
      })


    })
  }

  def addOrReplaceInvitationSent {
    addOrReplace(new Fragment("contactStatus", "invitationSent", PersonContactPanel.this))
  }

  def addOrReplaceUserIsContact {
    addOrReplace(new Fragment("contactStatus", "userIsContact", PersonContactPanel.this))
  }

  def addOrReplaceYourself {
    addOrReplace(new Fragment("contactStatus", "yourself", PersonContactPanel.this))
  }

  private def isInvitationReceivedAndWaitingForAcceptance(invitationReceivedOption: Option[Invitation]): Boolean = {
    if (invitationReceivedOption.isDefined) {
      val invitation = invitationReceivedOption.get
      !invitation.declined
    } else {
      false
    }
  }
}
