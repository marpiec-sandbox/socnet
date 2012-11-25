package pl.marpiec.socnet.web.component.wicket.form

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.CompoundPropertyModel
import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.form.{HiddenField, Form}
import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.wicket.{SecureAjaxButton, SecureFormModel}
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class OneButtonAjaxForm(id: String, buttonLabel: String, onClick: (AjaxRequestTarget) => Unit)
  extends OneLinkAjaxForm(id, buttonLabel, onClick)

