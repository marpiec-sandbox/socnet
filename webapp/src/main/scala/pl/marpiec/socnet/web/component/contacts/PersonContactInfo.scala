package pl.marpiec.socnet.web.component.contacts

import socnet.model.usercontacts.Contact
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.commons.lang.StringUtils
import org.apache.wicket.Component
import pl.marpiec.socnet.web.wicket.{SecureFormModel, SecureAjaxButton, SimpleStatelessForm}
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.markup.html.form.{Form, TextArea}

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

      val inviteForm: SimpleStatelessForm = new SimpleStatelessForm("inviteForm") {
        setOutputMarkupPlaceholderTag(true)

        setVisible(false)

        // var warningMessage: String = ""
        var invitationMessage: String = _

        //add(new Label("warningMessage"))
        add(new TextArea[String]("invitationMessage"))

        add(new AjaxButton("cancelButton") {

          def onSubmit(target: AjaxRequestTarget, form: Form[_]) {
            // invitationMessage = ""
            inviteLink.setVisible(true)
            inviteForm.setVisible(false)
            target.add(inviteLink)
            target.add(inviteForm)
          }

          def onError(target: AjaxRequestTarget, form: Form[_]) {}

        })

        add(new AjaxButton("submitButton") {

          // TODO add security token support


          def onSubmit(target: AjaxRequestTarget, form: Form[_]) {

          }

          def onError(target: AjaxRequestTarget, form: Form[_]) {}

          def onSecureSubmit(target: AjaxRequestTarget, formModel: SimpleStatelessForm) = {

         /*   if (StringUtils.isNotBlank(invitationMessage)) {

              //TODO send invitation

              invitationMessage = ""
              inviteLink.setVisible(true)
              inviteForm.setVisible(false)
              target.add(inviteLink)
              target.add(inviteForm)

            } else {
              // warningMessage = "Wiadomosc nie moze byc pusta"
            }   */

          }
        })
      }

      add(inviteForm)


    })

  }

}
