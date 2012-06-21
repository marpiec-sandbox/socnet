package pl.marpiec.socnet.web.page.profile.editUserProfilePage

import pl.marpiec.socnet.model.User
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.Component
import userSummaryPanel.{UserSummaryPreview, UserSummaryEditForm}

/**
 * @author Marcin Pieciukiewicz
 */

class UserSummaryPanel(id: String, val user: User) extends Panel(id) {


  //initialization
  setOutputMarkupId(true)

  //schema
  val userSummaryPreview = addAndReturn(new UserSummaryPreview("userSummaryPreview", user, this))
  val userSummaryForm = addAndReturn(new UserSummaryEditForm("userSummaryForm", user, this))


  //methods

  def switchToPreviewMode {
    userSummaryForm.setVisible(false)
    userSummaryPreview.setVisible(true)
  }

  def switchToEditMode {
    userSummaryForm.setVisible(true)
    userSummaryPreview.setVisible(false)
  }

  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }
}
