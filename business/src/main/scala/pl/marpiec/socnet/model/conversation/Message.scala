package pl.marpiec.socnet.model.conversation

import pl.marpiec.util.UID
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class Message(val id: UID, val messageText: String,
              val sentTime: LocalDateTime, val authorUserId: UID)


object Message {

  def createNewMessage(id: UID, messageText: String, authorUserId: UID): Message = {
    new Message(id, messageText, new LocalDateTime(), authorUserId)
  }

}