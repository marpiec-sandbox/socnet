package pl.marpiec.socnet.web.page.messages

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles

/**
 * @author Marcin Pieciukiewicz
 */

object MessagePreviewPage {

  val MESSAGE_ID_PARAM = "message_id"

}

class MessagePreviewPage extends SecureWebPage(SocnetRoles.USER) {

}

