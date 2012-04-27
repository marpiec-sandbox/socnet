package pl.marpiec.socnet.web.page.editUserProfilePage

import org.apache.wicket.markup.html.panel.Panel
import personalSummaryPanel.{PersonalSummaryEditForm, PersonalSummaryPreview}
import pl.marpiec.socnet.model.{User, UserProfile}
import scala.Predef._
import org.apache.wicket.Component


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class PersonalSummaryPanel(id: String, val user: User, val userProfile: UserProfile) extends Panel(id) {


  //initialization
  setOutputMarkupId(true)

  //schema
  val personalSummaryPreview = addAndReturn(new PersonalSummaryPreview("personalSummaryPreview", userProfile, this))
  val personalSummaryForm = addAndReturn(new PersonalSummaryEditForm("personalSummaryForm", userProfile, user, this))


  //methods

  def switchToPreviewMode {
    personalSummaryForm.setVisible(false)
    personalSummaryPreview.setVisible(true)
  }

  def switchToEditMode {
    personalSummaryForm.setVisible(true)
    personalSummaryPreview.setVisible(false)
  }

  private def addAndReturn[E <: Component](child: E): E = {
    add(child)
    child
  }

}
