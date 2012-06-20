package socnet.model

import conversation.Message
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class Conversation extends Aggregate(null, 0) {

  var creatorUserId: UID = _
  var title: String = ""
  var participantsUserIds: List[UID] = List()
  var conversationHiddenForUsers: Set[UID] = Set()
  var messages = List[Message]()

  def calculateUnreadMessagesCount(conversationInfoOption: Option[ConversationInfo]): Int = {
    if (conversationInfoOption.isDefined && conversationInfoOption.get.lastReadTime != null) {
      messages.count(message => message.sentTime.isAfter(conversationInfoOption.get.lastReadTime))
    } else {
      messages.size
    }
  }

  def copy = {
    BeanUtil.copyProperties(new Conversation, this)
  }

}
