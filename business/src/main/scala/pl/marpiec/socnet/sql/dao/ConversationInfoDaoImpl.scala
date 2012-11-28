package pl.marpiec.socnet.sql.dao

import pl.marpiec.socnet.sql.entity.ConversationInfo
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.joda.time.format.DateTimeFormat
import org.joda.time.LocalDateTime
import org.springframework.jdbc.support.rowset.SqlRowSet

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
      Option(mapRowSet(userId, conversationId, rowSet))
    } else {
      None
    }
  }
  
  private def mapRowSet(userId: UID, conversationId: UID, rowSet:SqlRowSet):ConversationInfo = {
    val conversationInfo = new ConversationInfo
    val lastReadTime = DateTimeFormat.forPattern(PATTERN).parseDateTime(rowSet.getString(1)).toLocalDateTime
    conversationInfo.conversationId = conversationId
    conversationInfo.userId = userId
    conversationInfo.lastReadTime = lastReadTime
    conversationInfo
  }

  def readMany(userId: UID, conversationIds: List[UID]) = {

    if(conversationIds.nonEmpty) {
      val comaSeparatedUids = conversationIds.map(_.uid).mkString(",")
      val rowSet = jdbcTemplate.queryForRowSet(
        "SELECT conversation_id, last_read_time FROM conversation_info WHERE user_id = ? AND conversation_id IN ("+comaSeparatedUids+")",
        Array(Long.box(userId.uid)): _*)

      var conversationInfoList = List[ConversationInfo]()

      while (rowSet.next()) {
        conversationInfoList ::= mapRowSetWithConversationId(userId, rowSet)
      }

      conversationInfoList
    } else {
      List[ConversationInfo]()
    }
  }


  private def mapRowSetWithConversationId(userId: UID, rowSet:SqlRowSet):ConversationInfo = {
    val conversationInfo = new ConversationInfo
    val lastReadTime = DateTimeFormat.forPattern(PATTERN).parseDateTime(rowSet.getString(2)).toLocalDateTime
    conversationInfo.conversationId = new UID(rowSet.getLong(1))
    conversationInfo.userId = userId
    conversationInfo.lastReadTime = lastReadTime
    conversationInfo
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
      //TODO ignore situation when row is already in
      conversation
    }
  }

  def update(conversationInfo: ConversationInfo) {
    jdbcTemplate.update("UPDATE conversation_info SET last_read_time = ? WHERE user_id = ? AND conversation_id = ?",
      Array(conversationInfo.lastReadTime.toString(PATTERN), Long.box(conversationInfo.userId.uid), Long.box(conversationInfo.conversationId.uid)): _*)
  }
}
