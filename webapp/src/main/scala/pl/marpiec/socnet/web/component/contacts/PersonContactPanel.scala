package pl.marpiec.socnet.web.component.contacts

import model.InviteUserFormModel
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.UserContacts
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.usercontacts.UserContactsCommand
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.Component
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.form.TextArea
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.web.wicket.SecureFormModel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.web.page.conversation.StartConversationPage
import org.apache.wicket.markup.html.panel.{EmptyPanel, Fragment, Panel}
import pl.marpiec.socnet.model.usercontacts.Invitation

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

  val itsCurrentUser = userId == currentUser.id
  var contactAcceptedOnThisPage = false
  var inviteLink: Component = null

  setOutputMarkupId(true)

  add(StartConversationPage.getLink("newConversationLink", userId).setVisible(!itsCurrentUser))

  addOrReplaceContactLevel()

  private def usersHaveCommonContact(): Boolean = {

    var haveCommonContacts = false

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


  private def addOrReplaceContactLevel() {
    if (getSession.asInstanceOf[SocnetSession].userId == userId) {
      addOrReplace(new Label("contactLevel", "Twój profil"))
    } else if (loggedInUserContacts.contactByUserId(userId).isDefined || contactAcceptedOnThisPage) {
      addOrReplace(new Label("contactLevel", "Twój kontakt"))
    } else if (usersHaveCommonContact) {
      addOrReplace(new Label("contactLevel", "Kontakt 2-go stopnia"))
    } else {
      addOrReplace(new Label("contactLevel", "Nieznajomy"))
    }
  }

  def addOrReplaceInviteContact {

    var inviteForm: Component = null

    addOrReplace(new Fragment("inviteFormHolder", "inviteContactForm", PersonContactPanel.this) {

      inviteForm = new StandardAjaxSecureForm[InviteUserFormModel]("inviteForm") {

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


    addOrReplace(new Fragment("contactButtonHolder", "inviteContactButton", PersonContactPanel.this) {

      inviteLink = new AjaxLink("inviteLink") {
        setOutputMarkupPlaceholderTag(true)

        def onClick(target: AjaxRequestTarget) {
          inviteLink.setVisible(false)
          inviteForm.setVisible(true)
          target.add(PersonContactPanel.this)
        }
      }
      add(inviteLink)
    })

  }

  def addOrReplaceInvitationReceived {
    val invitationReceived = invitationReceivedOption.get

    addOrReplace(new Fragment("inviteFormHolder", "invitationReceivedForm", PersonContactPanel.this) {

      add(new StandardAjaxSecureForm[SecureFormModel]("replyForInvitationForm") {
        def initialize {
          setModel(new CompoundPropertyModel[SecureFormModel](new SecureFormModel))
        }

        def buildSchema {} //do nothing

        def onSecureSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {
          userContactsCommand.acceptInvitation(currentUser.id, userContacts.id, userId, invitationReceived.id)

          contactAcceptedOnThisPage = true
          addOrReplaceUserIsContact
          addOrReplaceContactLevel

          target.add(PersonContactPanel.this)
        }

        def onSecureCancel(target: AjaxRequestTarget, formModel: SecureFormModel) {
          userContactsCommand.declineInvitation(currentUser.id, userContacts.id, userId, invitationReceived.id)

          addOrReplaceInviteContact

          target.add(PersonContactPanel.this)
        }
      })
    })
    addOrReplace(new EmptyPanel("contactButtonHolder"))
  }

  def addOrReplaceInvitationSent {
    addOrReplace(new Fragment("inviteFormHolder", "invitationSent", PersonContactPanel.this))
    addOrReplace(new EmptyPanel("contactButtonHolder"))
  }

  def addOrReplaceUserIsContact {
    addOrReplace(new EmptyPanel("contactButtonHolder"))
    addOrReplace(new EmptyPanel("inviteFormHolder"))
  }

  def addOrReplaceYourself {
    addOrReplace(new EmptyPanel("contactButtonHolder"))
    addOrReplace(new EmptyPanel("inviteFormHolder"))
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
