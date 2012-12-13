package pl.marpiec.socnet.web.page.conversation.conversationPage

import pl.marpiec.util.UID
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.model.User

trait PageWithInvitingPeopleSupport {

  def userIsInvitedOrParticipating(userId:UID):Boolean

  def updateConversationData(target:AjaxRequestTarget)

  def newInvitedPeopleAdded(users: List[User])
}
