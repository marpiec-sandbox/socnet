package pl.marpiec.cqrs

import org.apache.commons.lang.RandomStringUtils
import java.sql.Timestamp
import java.util.Date
import pl.marpiec.util.{JsonSerializer, UID}
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("triggeredEventsDatabase")
class TriggeredEventsDatabaseImpl @Autowired()(val jdbcTemplate: JdbcTemplate) extends TriggeredEvents {

  private val TRIGGER_LENGTH = 128
  private val MILLS_IN_24H = 86400000


  var jsonSerializer = new JsonSerializer

  def addNewTriggeredEvent(userId: UID, event: Event): String = {

    val trigger = generateRandomTrigger

    jdbcTemplate.update("INSERT INTO trigger_events (id, user_uid, creation_time, execution_time, trigger, event_type, event) " +
      "VALUES (NEXTVAL('trigger_events_seq'), ?, ?, NULL, ?, ?, ?)",
      Array(Long.box(userId.uid),
        new Timestamp(new Date().getTime),
        trigger, event.getClass.getName, jsonSerializer.toJson(event)): _*)

    trigger
  }


  def getEventForTrigger(trigger: String): Option[Event] = {
    val resultOption = getUserIdAndEventForTrigger(trigger)
    if (resultOption.isDefined) {
      val (userId, event) = resultOption.get
      Option(event)
    } else {
      None
    }
  }

  def getUserIdAndEventForTrigger(trigger: String): (Option[(UID, Event)]) = {
    val result = jdbcTemplate.queryForRowSet("SELECT user_uid, creation_time, event_type, event " +
      "FROM trigger_events WHERE execution_time IS NULL AND trigger = ?",
      Array(trigger): _*)

    if (result.next()) {

      val userId = new UID(result.getLong(1))
      val creationTime = result.getTimestamp(2)
      val eventType = result.getString(3)
      val event = result.getString(4)

      val currentTime = (new Date).getTime

      if (currentTime - creationTime.getTime < MILLS_IN_24H) {
        val eventObject = jsonSerializer.fromJson(event, Class.forName(eventType)).asInstanceOf[Event]
        Option[(UID, Event)]((userId, eventObject))
      } else {
        None
      }

    } else {
      None
    }
  }


  def markEventAsExecuted(trigger: String) = {
    val updateEvent = jdbcTemplate.update("UPDATE trigger_events SET execution_time=? " +
      "WHERE execution_time IS NULL AND trigger = ?",
      Array(new Timestamp((new Date).getTime), trigger): _*)
  }

  private def generateRandomTrigger(): String = {
    return RandomStringUtils.randomAlphanumeric(TRIGGER_LENGTH);
  }
}

