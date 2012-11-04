package pl.marpiec.socnet.web.component.contacts

import model.InviteUserFormModel
import pl.marpiec.util.UID
import pl.marpiec.socnet.model.{ContactInvitation, UserContacts}
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
import pl.marpiec.socnet.service.contactinvitation.ContactInvitationCommand

/**
 * @author Marcin Pieciukiewicz
 */

class PersonContactPanel(id: String, previewedUserId: UID,
                         previewedUserContacts: UserContacts,
                         loggedInUserContacts: UserContacts,
                         invitationOption: Option[ContactInvitation]) extends Panel(id) {

  @SpringBean private var contactInvitationCommand: ContactInvitationCommand = _
  @SpringBean private var userContactsCommand: UserContactsCommand = _
  @SpringBean private var uidGenerator: UidGenerator = _

  val currentUser = getSession.asInstanceOf[SocnetSession].user

  val currentUserIsPreviewedUserContact = previewedUserContacts.contactsIds.contains(currentUser.id)
  val invitationWasSent = userSentInvitation()
  val invitationWasReceived = userReceivedInvitation()


  val itsCurrentUser = previewedUserId == currentUser.id
  var contactAcceptedOnThisPage = false
  var inviteLink: Component = null

  setOutputMarkupId(true)

  add(StartConversationPage.getLink("newConversationLink", previewedUserId).setVisible(!itsCurrentUser))
  addOrReplaceContactLevel()

  if (itsCurrentUser) {
    addOrReplaceYourself
  } else if (currentUserIsPreviewedUserContact) {
    addOrReplaceUserIsContact
  } else if (invitationWasReceived) {
    addOrReplaceInvitationReceived
  } else if (invitationWasSent) {
    addOrReplaceInvitationSent
  } else {
    addOrReplaceInviteContact
  }

  def usersHaveCommonContact(): Boolean = {
    previewedUserContacts.contactsIds.filter(loggedInUserContacts.contactsIds.contains(_)).size > 0
  }

  def userSentInvitation(): Boolean = {
    if (invitationOption.isDefined) {
      invitationOption.get.senderUserId == currentUser.id
    } else {
      false
    }
  }

  def userReceivedInvitation(): Boolean = {
    if (invitationOption.isDefined) {
      invitationOption.get.receiverUserId == currentUser.id
    } else {
      false
    }
  }

  private def addOrReplaceContactLevel() {
    if (getSession.asInstanceOf[SocnetSession].userId == previewedUserId) {
      addOrReplace(new Label("contactLevel", "Twój profil"))
    } else if (loggedInUserContacts.contactsIds.contains(previewedUserId) || contactAcceptedOnThisPage) {
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

            contactInvitationCommand.sendInvitation(currentUser.id,
              previewedUserId, formModel.inviteMessage, uidGenerator.nextUid)
            //TODO handle send invitation exceptions

            formModel.inviteMessage = ""
            formModel.warningMessage = ""
            inviteLink.setVisible(true)
            inviteForm.setVisible(false)
            target.add(inviteLink)
            target.add(inviteForm)

            addOrReplaceInvitationSent

          } else {
            formModel.warningMessage = "Wiadomość nie może być pusta"
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
    val invitationReceived = invitationOption.get

    addOrReplace(new Fragment("inviteFormHolder", "invitationReceivedForm", PersonContactPanel.this) {

      add(new StandardAjaxSecureForm[SecureFormModel]("replyForInvitationForm") {
        def initialize {
          setModel(new CompoundPropertyModel[SecureFormModel](new SecureFormModel))
        }

        def buildSchema {} //do nothing

        def onSecureSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {
          contactInvitationCommand.acceptInvitation(currentUser.id, invitationReceived.id, invitationReceived.version, invitationReceived.senderUserId, invitationReceived.receiverUserId)

          contactAcceptedOnThisPage = true
          addOrReplaceUserIsContact
          addOrReplaceContactLevel

          target.add(PersonContactPanel.this)
        }

        def onSecureCancel(target: AjaxRequestTarget, formModel: SecureFormModel) {
          contactInvitationCommand.declineInvitation(currentUser.id, invitationReceived.id, invitationReceived.version)

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

}
