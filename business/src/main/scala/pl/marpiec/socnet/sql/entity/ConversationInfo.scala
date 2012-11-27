package pl.marpiec.socnet.sql.entity

import pl.marpiec.util.UID
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */
class ConversationInfo {
  var userId: UID = _
  var conversationId: UID = _

  var lastReadTime: LocalDateTime = new LocalDateTime(0)

}
