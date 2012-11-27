package pl.marpiec.socnet.sql.dao

import pl.marpiec.socnet.sql.entity.ConversationInfo
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.joda.time.format.DateTimeFormat
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("conversationInfoDao")
class ConversationInfoDaoImpl @Autowired()(val jdbcTemplate: JdbcTemplate) extends ConversationInfoDao {

  val PATTERN = "yyyy-MM-dd HH:mm:ss:SSS"


  def read(userId: UID, conversationId: UID): Option[ConversationInfo] = {

    val rowSet = jdbcTemplate.queryForRowSet("SELECT last_read_time FROM conversation_info WHERE user_id = ? AND conversation_id = ?",
      Array(Long.box(userId.uid), Long.box(conversationId.uid)): _*)

    if (rowSet.next()) {
      val lastReadTime = DateTimeFormat.forPattern(PATTERN).parseDateTime(rowSet.getString(1)).toLocalDateTime
      val conversationInfo = new ConversationInfo
      conversationInfo.conversationId = conversationId
      conversationInfo.userId = userId
      conversationInfo.lastReadTime = lastReadTime
      Option(conversationInfo)
    } else {
      None
    }
  }


  def readMany(userId: UID, conversationIds: List[UID]) = {
    //TODO!!!!
    var conversationInfoList = List[ConversationInfo]()
    conversationIds.foreach(conversationId => {
      val conversationInfoOption = read(userId, conversationId)
      if(conversationInfoOption.isDefined) {
        conversationInfoList ::= conversationInfoOption.get
      }
    })
    conversationInfoList
  }

  def readOrCreate(userId: UID, conversationId: UID) = {
    val conversationOption = read(userId, conversationId)
    if (conversationOption.isDefined) {
      conversationOption.get
    } else {
      val conversation = new ConversationInfo
      conversation.userId = userId
      conversation.conversationId = conversationId
      jdbcTemplate.update("INSERT INTO conversation_info (id, user_id, conversation_id, last_read_time) VALUES (NEXTVAL('conversation_info_seq'), ?, ?, ?)",
        Array(Long.box(userId.uid), Long.box(conversationId.uid), new LocalDateTime(0).toString(PATTERN)): _*)
      conversation
    }
  }

  def update(conversationInfo: ConversationInfo) {
    jdbcTemplate.update("UPDATE conversation_info SET last_read_time = ? WHERE user_id = ? AND conversation_id = ?",
      Array(conversationInfo.lastReadTime.toString(PATTERN), Long.box(conversationInfo.userId.uid), Long.box(conversationInfo.conversationId.uid)): _*)
  }
}
