package pl.marpiec.socnet.web.component.user

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.model.User
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.HiddenField
import org.apache.wicket.model.Model
import pl.marpiec.util.IdProtectionUtil

/**
 * @author Marcin Pieciukiewicz
 */

class UserSummaryPreviewNoLinkPanel(id: String, user: User) extends Panel(id) {

  add(new Label("userName", user.fullName))
  add(new Label("summary", user.summary))
  add(new HiddenField[String]("k", new Model(IdProtectionUtil.encrypt(user.id))) {
    override def getInputName = ""
  })

}
