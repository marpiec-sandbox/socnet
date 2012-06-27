package pl.marpiec.socnet.web.component.contacts

import model.InviteUserFormModel
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.Component
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.form.TextArea
import org.apache.commons.lang.StringUtils
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.service.usercontacts.UserContactsCommand
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.util.UID
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.socnet.model.usercontacts.Invitation

/**
 * @author Marcin Pieciukiewicz
 */

class PersonContactInvitationPanel(id: String, userId: UID, userContacts: UserContacts) extends Panel(id) {

  @SpringBean
  private var userContactsCommand: UserContactsCommand = _

  @SpringBean
  private var uidGenerator: UidGenerator = _

  val contactOption = userContacts.contactByUserId(userId)
  val invitationSentOption = userContacts.invitationSentByUserId(userId)
  val invitationReceivedOption = userContacts.invitationReceivedByUserId(userId)

  val currentUser = getSession.asInstanceOf[SocnetSession].user

  setOutputMarkupId(true)

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
    addOrReplace(new Fragment("contactStatus", "inviteContact", PersonContactInvitationPanel.this) {

      val inviteLink: Component = new AjaxLink("inviteLink") {
        setOutputMarkupPlaceholderTag(true)

        def onClick(target: AjaxRequestTarget) {
          inviteLink.setVisible(false)
          inviteForm.setVisible(true)
          target.add(PersonContactInvitationPanel.this)
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
          target.add(PersonContactInvitationPanel.this)
        }

        def onSecureCancel(target: AjaxRequestTarget, formModel: InviteUserFormModel) {
          formModel.inviteMessage = ""
          formModel.warningMessage = ""
          inviteLink.setVisible(true)
          inviteForm.setVisible(false)
          target.add(PersonContactInvitationPanel.this)
        }

      }

      add(inviteForm)
    })
  }

  def addOrReplaceInvitationReceived {
    val invitationReceived = invitationReceivedOption.get

    addOrReplace(new Fragment("contactStatus", "invitationReceived", PersonContactInvitationPanel.this) {

      add(new StandardAjaxSecureForm[SecureFormModel]("replyForInvitationForm") {
        def initialize {
          setModel(new CompoundPropertyModel[SecureFormModel](new SecureFormModel))
        }

        def buildSchema {} //do nothing

        def onSecureSubmit(target: AjaxRequestTarget, formModel: SecureFormModel) {
          userContactsCommand.acceptInvitation(currentUser.id, userContacts.id, userId, invitationReceived.id)

          addOrReplaceUserIsContact

          target.add(PersonContactInvitationPanel.this)
        }

        def onSecureCancel(target: AjaxRequestTarget, formModel: SecureFormModel) {
          userContactsCommand.declineInvitation(currentUser.id, userContacts.id, userId, invitationReceived.id)

          addOrReplaceInviteContact

          target.add(PersonContactInvitationPanel.this)
        }
      })


    })
  }

  def addOrReplaceInvitationSent {
    addOrReplace(new Fragment("contactStatus", "invitationSent", PersonContactInvitationPanel.this))
  }

  def addOrReplaceUserIsContact {
    addOrReplace(new Fragment("contactStatus", "userIsContact", PersonContactInvitationPanel.this))
  }

  def addOrReplaceYourself {
    addOrReplace(new Fragment("contactStatus", "yourself", PersonContactInvitationPanel.this))
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
