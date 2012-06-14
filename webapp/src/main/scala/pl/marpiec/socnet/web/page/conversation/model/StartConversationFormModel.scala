package pl.marpiec.socnet.web.page.conversation.model

import pl.marpiec.socnet.web.wicket.SecureFormModel

/**
 * @author Marcin Pieciukiewicz
 */

class StartConversationFormModel extends SecureFormModel {
  var conversationTitle: String = ""
  var messageText: String = ""
}
