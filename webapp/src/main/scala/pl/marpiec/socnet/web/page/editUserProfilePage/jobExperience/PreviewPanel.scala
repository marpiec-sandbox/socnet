package pl.marpiec.socnet.web.page.editUserProfilePage.jobExperience

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.model.UserProfile
import pl.marpiec.socnet.web.page.editUserProfilePage.elementListPanel.{ElementListPanel, ElementPanel}

/**
 * @author Marcin Pieciukiewicz
 */

class PreviewPanel[T, TM](id: String, mainListPanel: ElementListPanel[T, TM], parent: ElementPanel[T, TM], val element: T,
                      val userProfile: UserProfile) extends Panel(id) {


  mainListPanel.buildPreviewSchema(this, element)

  add(new AjaxFallbackLink("editButton") {
    def onClick(target: AjaxRequestTarget) {
      parent.switchToEditMode
      target.add(parent)
    }
  })

  add(new AjaxFallbackLink("deleteButton") {
    def onClick(target: AjaxRequestTarget) {

      mainListPanel.removeElement(element)
      userProfile.incrementVersion
      parent.hideRemovedElement
      target.add(parent)
    }
  })


}