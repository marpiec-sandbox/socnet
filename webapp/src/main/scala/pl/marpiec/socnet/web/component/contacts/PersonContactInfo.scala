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
import socnet.service.usercontacts.UserContactsCommand
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.util.UID
import pl.marpiec.cqrs.UidGenerator
import pl.marpiec.socnet.web.component.wicket.form.StandardAjaxSecureForm
import socnet.model.UserContacts

/**
 * @author Marcin Pieciukiewicz
 */

class PersonContactInfo(id: String, userId:UID, userContacts: UserContacts) extends Panel(id) {

  @SpringBean
  private var userContactsCommand: UserContactsCommand = _

  @SpringBean
  private var uidGenerator: UidGenerator = _

  val contactOption = userContacts.contactByUserId(userId)
  val invitationSentOption = userContacts.invitationsSentByUserId(userId)
  val invitationReceivedOption = userContacts.invitationsReceivedByUserId(userId)

  val currentUser = getSession.asInstanceOf[SocnetSession].user

  setOutputMarkupId(true)

  if (userContacts.userId == userId) {

    add(new Fragment("contactStatus", "yourself", PersonContactInfo.this))

  } else if (contactOption.isDefined) {

    add(new Fragment("contactStatus", "userIsContact", PersonContactInfo.this))

  } else if (invitationSentOption.isDefined) {

    add(new Fragment("contactStatus", "invitationSent", PersonContactInfo.this))

  } else if (invitationReceivedOption.isDefined) {

    add(new Fragment("contactStatus", "invitationReceived", PersonContactInfo.this))

  } else {

    add(new Fragment("contactStatus", "inviteContact", PersonContactInfo.this) {

      val inviteLink: Component = new AjaxLink("inviteLink") {
        setOutputMarkupPlaceholderTag(true)

        def onClick(target: AjaxRequestTarget) {
          inviteLink.setVisible(false)
          inviteForm.setVisible(true)
          target.add(PersonContactInfo.this)
        }
      }
      add(inviteLink)


      val inviteForm: StandardAjaxSecureForm[InviteUserFormModel] = new StandardAjaxSecureForm[InviteUserFormModel]("inviteForm") {

          def initialize {
            setModel(new CompoundPropertyModel[InviteUserFormModel](new InviteUserFormModel))
            setOutputMarkupPlaceholderTag(true)
            setVisible(false)
          }

          def buildSchema {
            add(new TextArea[String]("inviteMessage"))
          }

          def onSecureSubmit(target: AjaxRequestTarget, formModel: InviteUserFormModel) {
            if (StringUtils.isNotBlank(formModel.inviteMessage)) {

              userContactsCommand.sendInvitation(currentUser.id, userContacts.id, 0,
                userId, formModel.inviteMessage, uidGenerator.nextUid)
              //TODO handle send invitation exceptions

              formModel.inviteMessage = ""
              formModel.warningMessage = ""
              inviteLink.setVisible(true)
              inviteForm.setVisible(false)
              target.add(inviteLink)
              target.add(inviteForm)

              PersonContactInfo.this.addOrReplace(new Fragment("contactStatus", "invitationSent", PersonContactInfo.this))


            } else {
              formModel.warningMessage = "Wiadomosc nie moze byc pusta"
            }
            target.add(PersonContactInfo.this)
          }

          def onSecureCancel(target: AjaxRequestTarget, formModel: InviteUserFormModel) {
            formModel.inviteMessage = ""
            formModel.warningMessage = ""
            inviteLink.setVisible(true)
            inviteForm.setVisible(false)
            target.add(PersonContactInfo.this)
          }

        }

      add(inviteForm)
    })
  }
}
