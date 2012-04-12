package pl.marpiec.cqrs

import pl.marpiec.util.UID
import collection.mutable.{HashMap, ListBuffer}
import java.sql._
import com.google.gson.Gson
import java.util.{UUID, Date}

/**
 * @author Marcin Pieciukiewicz
 */

class EventStoreDbImpl extends EventStore {

  private val connection:Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
  private val listeners = new ListBuffer[EventStoreListener]

  def getEventsForEntity(entityClass: Class[_], id: UID):ListBuffer[CqrsEvent] = {

    var gson = new Gson

    val selectEvents = connection.prepareStatement("SELECT event, event_type FROM events WHERE aggregate_uid = ? ORDER BY event_time")
    selectEvents.setObject(1, id.uuid)
    val results = selectEvents.executeQuery

    val events = new ListBuffer[CqrsEvent]

    while(results.next()) {

      var event = results.getString(1)
      var eventType = results.getString(2)

      events += gson.fromJson(event, Class.forName(eventType)).asInstanceOf[CqrsEvent]

    }
    events
  }

  def addEvent(event: CqrsEvent) {

    var gson = new Gson

    val insert = connection.prepareStatement("INSERT INTO events (aggregate_uid, event_time, version, event_type, event) VALUES (?, ?, ?, ?, ?)")
    insert.setObject(1, event.entityId.uuid)
    insert.setTimestamp(2, new Timestamp(new Date().getTime))
    insert.setInt(3, event.expectedVersion)
    insert.setString(4, event.getClass.getName)
    insert.setObject(5, gson.toJson(event))
    insert.executeUpdate

    val update = connection.prepareStatement("UPDATE aggregates SET version = version + 1 WHERE uid = ?")
    update.setObject(1, event.entityId.uuid)
    update.executeUpdate

    callAllListenersAboutNewEvent(event.entityClass, event.entityId)

  }

  def addEventForNewAggregate(id: UID, event: CqrsEvent) {

    event.entityId = id
    var gson = new Gson()

    val insertAggregate = connection.prepareStatement("INSERT INTO aggregates (class, uid, version) VALUES (?,?,?)")
    insertAggregate.setString(1, event.entityClass.getName)
    insertAggregate.setObject(2, id.uuid)
    insertAggregate.setInt(3, 1)
    insertAggregate.executeUpdate

    val insertEvent = connection.prepareStatement("INSERT INTO events (aggregate_uid, event_time, version, event_type, event) VALUES (?, ?, ?, ?, ?)")
    insertEvent.setObject(1, event.entityId.uuid)
    insertEvent.setTimestamp(2, new Timestamp(new Date().getTime))
    insertEvent.setInt(3, event.expectedVersion)
    insertEvent.setString(4, event.getClass.getName)
    insertEvent.setObject(5, gson.toJson(event))
    insertEvent.executeUpdate

    callAllListenersAboutNewEvent(event.entityClass, event.entityId)

  }

  def initDatabaseIfNotExists {

    val createEvents = connection.prepareStatement("CREATE TABLE IF NOT EXISTS events (aggregate_uid UUID, event_time TIMESTAMP, version INT, event_type VARCHAR(128), event VARCHAR(10240))")
    createEvents.execute()

    val createAggregates = connection.prepareStatement("CREATE TABLE IF NOT EXISTS aggregates (class VARCHAR(128), uid UUID, version INT)")
    createAggregates.execute()
  }

  def callListenersForAllAggregates {
    val selectAggregates = connection.prepareStatement("SELECT class, uid FROM aggregates")
    val results = selectAggregates.executeQuery
    while(results.next()) {
      var className = results.getString(1)
      var uuid = results.getObject(2).asInstanceOf[UUID]
      callAllListenersAboutNewEvent(Class.forName(className).asInstanceOf[Class[_ <: CqrsEntity]], UID.fromUUID(uuid))
    }
  }


  def addListener(listener: EventStoreListener) {
    listeners += listener
  }

  private def callAllListenersAboutNewEvent(entityClass:Class[_ <: CqrsEntity], entityId:UID) {
    listeners.foreach(listener => listener.onEntityChanged(entityClass, entityId))
  }
}
