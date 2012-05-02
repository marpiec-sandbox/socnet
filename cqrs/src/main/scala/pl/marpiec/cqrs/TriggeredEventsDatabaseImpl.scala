package pl.marpiec.cqrs

import org.apache.commons.lang.RandomStringUtils
import java.sql.{Timestamp, DriverManager, Connection}
import java.util.Date
import pl.marpiec.util.{JsonSerializer, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class TriggeredEventsDatabaseImpl(val connectionPool:DatabaseConnectionPool) extends TriggeredEvents {

  private val TRIGGER_LENGTH = 128
  private val MILLS_IN_24H = 86400000


  var jsonSerializer = new JsonSerializer

  def addNewTriggeredEvent(userId: UID, event: Event):String = {

    val trigger = generateRandomTrigger

    val insertEvent = connectionPool.getConnection.prepareStatement("INSERT INTO trigger_events (id, user_uid, creation_time, execution_time, trigger, event_type, event) " +
      "VALUES (NEXTVAL('trigger_events_seq'), ?, ?, NULL, ?, ?, ?)")

    insertEvent.setLong(1, userId.uid)
    insertEvent.setTimestamp(2, new Timestamp(new Date().getTime))
    insertEvent.setString(3, trigger)
    insertEvent.setString(4, event.getClass.getName)
    insertEvent.setString(5, jsonSerializer.toJson(event))

    insertEvent.execute

    trigger
  }


  def getEventForTrigger(trigger: String):Option[Event] = {
    val selectTrigger = connectionPool.getConnection.prepareStatement("SELECT creation_time, event_type, event FROM trigger_events WHERE execution_time IS NULL AND trigger = ?")

    selectTrigger.setString(1, trigger)

    val result = selectTrigger.executeQuery()
    
    if(result.next()) {

      val creationTime = result.getTimestamp(1)
      val eventType = result.getString(2)
      val event = result.getString(3)

      val currentTime = (new Date).getTime
      
      if(currentTime - creationTime.getTime < MILLS_IN_24H) {
        

        val eventObject = jsonSerializer.fromJson(event, Class.forName(eventType)).asInstanceOf[Event]
        Option[Event](eventObject)        
      } else {
        None
      }

    } else {
      None
    }
  }


  def markEventAsExecuted(trigger: String) = {
    val updateEvent = connectionPool.getConnection.prepareStatement("UPDATE trigger_events SET execution_time=? WHERE execution_time IS NULL AND trigger = ?")

    updateEvent.setTimestamp(1, new Timestamp((new Date).getTime))
    updateEvent.setString(2, trigger)
    

    updateEvent.executeUpdate()
  }

  private def generateRandomTrigger():String = {
    return RandomStringUtils.randomAlphanumeric(TRIGGER_LENGTH);
  }
}

