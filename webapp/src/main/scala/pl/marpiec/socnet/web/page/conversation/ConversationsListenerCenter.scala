package pl.marpiec.socnet.web.page.conversation

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

object ConversationsListenerCenter {

  var listeners = Map[UID, Set[ConversationListener]]()

  def addListener(conversationId: UID, listener: ConversationListener) {
    var conversationListeners = listeners.getOrElse(conversationId, Set[ConversationListener]())
    conversationListeners += listener
    listeners += conversationId -> conversationListeners
  }

  def removeListener(conversationId: UID, listener: ConversationListener) {
    var conversationListeners = listeners.getOrElse(conversationId, Set[ConversationListener]())
    conversationListeners = conversationListeners.filterNot(_ == listener)
    listeners += conversationId -> conversationListeners
  }

  def conversationChanged(conversationId:UID) {
    val conversationListeners = listeners.getOrElse(conversationId, Set[ConversationListener]())

    conversationListeners.foreach(listener => {
      removeListener(conversationId, listener)
      listener.onConversationChanged()
    })
  }

}
