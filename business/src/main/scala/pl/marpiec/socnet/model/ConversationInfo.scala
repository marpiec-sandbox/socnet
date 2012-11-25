package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{UID, BeanUtil}
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class ConversationInfo extends Aggregate(null, 0) {

  var userId: UID = _
  var conversationId: UID = _

  var lastReadTime: LocalDateTime = new LocalDateTime(0)

  def copy = {
    BeanUtil.copyProperties(new ConversationInfo, this)
  }
}
