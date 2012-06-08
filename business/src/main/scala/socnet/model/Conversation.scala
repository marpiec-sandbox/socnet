package socnet.model

import conversation.Message
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class Conversation extends Aggregate(null, 0) {

  var creatorUserId:UID = _
  var title:String = ""
  var participantsUserIds:List[UID] = List()
  var messages = List[Message]()

  def copy = {
    BeanUtil.copyProperties(new Conversation, this)
  }

}
