package pl.marpiec.socnet.model

import conversation.Message
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{BeanUtil, UID}
import pl.marpiec.socnet.sql.entity.ConversationInfo

/**
 * @author Marcin Pieciukiewicz
 */

class Conversation extends Aggregate(null, 0) {

  var creatorUserId: UID = _
  var title: String = ""
  var participantsUserIds = Set[UID]()
  var invitedUserIds = Set[UID]()
  var previousUserIds = Set[UID]()

  var messages = List[Message]()

  def calculateUnreadMessagesCount(conversationInfoOption: Option[ConversationInfo]): Int = {
    if (conversationInfoOption.isDefined && conversationInfoOption.get.lastReadTime != null) {
      messages.count(message => message.sentTime.isAfter(conversationInfoOption.get.lastReadTime))
    } else {
      messages.size
    }
  }

  def userParticipating(userId:UID) = participantsUserIds.contains(userId)

  def userInvited(userId:UID) = invitedUserIds.contains(userId)

  def getAllUsersList = (participantsUserIds ++ invitedUserIds ++ previousUserIds).toList

  def copy = {
    BeanUtil.copyProperties(new Conversation, this)
  }

}
