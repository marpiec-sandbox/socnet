package pl.marpiec.socnet.sql.entity

import pl.marpiec.util.UID
import org.joda.time.LocalDateTime
import javax.persistence.{Column, Entity, Id}
import org.hibernate.annotations.Type

/**
 * @author Marcin Pieciukiewicz
 */
@Entity
class ConversationInfo {

  @Id
  var userId: UID = _
  @Column
  var conversationId: UID = _

  @Column
  @Type(`type` = "org.joda.time.contrib.hibernate.PersistentLocalDateTime")
  var lastReadTime: LocalDateTime = new LocalDateTime(0)

}
