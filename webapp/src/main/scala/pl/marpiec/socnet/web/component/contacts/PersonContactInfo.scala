package pl.marpiec.socnet.web.component.contacts

import model.InviteUserFormModel
import socnet.model.usercontacts.Contact
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.Component
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.form.TextArea
import pl.marpiec.socnet.web.wicket.{SecureForm, SecureAjaxButton}
import org.apache.wicket.markup.html.basic.Label
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

class PersonContactInfo(id: String, contactOption: Option[Contact]) extends Panel(id) {

  val isContact = contactOption.isDefined


  if (isContact) {

    add(new Fragment("contactStatus", "userIsContact", PersonContactInfo.this))


  } else {
    add(new Fragment("contactStatus", "inviteContact", PersonContactInfo.this) {

      val inviteLink: Component = new AjaxLink("inviteLink") {
        setOutputMarkupPlaceholderTag(true)

        def onClick(target: AjaxRequestTarget) {
          inviteLink.setVisible(false)
          inviteForm.setVisible(true)
          target.add(inviteLink)
          target.add(inviteForm)
        }
      }

      add(inviteLink)

      val inviteForm: SecureForm[InviteUserFormModel] = new SecureForm[InviteUserFormModel]("inviteForm") {

        def initialize {
          setModel(new CompoundPropertyModel[InviteUserFormModel](new InviteUserFormModel))

          setOutputMarkupPlaceholderTag(true)
          setVisible(false)

        }

        def buildSchema {

          add(new Label("warningMessage"))
          add(new TextArea[String]("inviteMessage"))

          add(new SecureAjaxButton[InviteUserFormModel]("cancelButton") {

            def onSecureSubmit(target: AjaxRequestTarget, formModel: InviteUserFormModel) {
              formModel.inviteMessage = ""
              formModel.warningMessage = ""
              inviteLink.setVisible(true)
              inviteForm.setVisible(false)
              target.add(inviteLink)
              target.add(inviteForm)
            }
          })

          add(new SecureAjaxButton[InviteUserFormModel]("submitButton") {

            def onSecureSubmit(target: AjaxRequestTarget, formModel: InviteUserFormModel) {

              if (StringUtils.isNotBlank(formModel.inviteMessage)) {

                //TODO send invitation

                formModel.inviteMessage = ""
                formModel.warningMessage = ""
                inviteLink.setVisible(true)
                inviteForm.setVisible(false)
                target.add(inviteLink)
                target.add(inviteForm)

              } else {
                formModel.warningMessage = "Wiadomosc nie moze byc pusta"
                target.add(inviteForm)
              }

            }

          })

        }


      }

      add(inviteForm)


    })

  }

}
