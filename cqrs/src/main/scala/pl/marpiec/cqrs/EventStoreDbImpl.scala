package pl.marpiec.cqrs

import collection.mutable.ListBuffer
import java.sql._
import java.util.{UUID, Date}
import pl.marpiec.util.{JsonUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class EventStoreDbImpl extends EventStore {

  private val connection: Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
  private val listeners = new ListBuffer[EventStoreListener]

  var jsonSerializer = new JsonUtil

  def getEventsForEntity(entityClass: Class[_], id: UID): ListBuffer[CqrsEvent] = {

    val selectEvents = connection.prepareStatement("SELECT event, event_type FROM events WHERE aggregate_uid = ? ORDER BY event_time")
    selectEvents.setLong(1, id.uid)
    val results = selectEvents.executeQuery

    val events = new ListBuffer[CqrsEvent]

    while(results.next()) {

      var event = results.getString(1)
      var eventType = results.getString(2)

      events += jsonSerializer.fromJson(event, Class.forName(eventType)).asInstanceOf[CqrsEvent]

    }
    
    events
  }

  def addEvent(event: CqrsEvent) {

    val insert = connection.prepareStatement("INSERT INTO events (aggregate_uid, event_time, version, event_type, event) VALUES (?, ?, ?, ?, ?)")
    insert.setLong(1, event.entityId.uid)
    insert.setTimestamp(2, new Timestamp(new Date().getTime))
    insert.setInt(3, event.expectedVersion)
    insert.setString(4, event.getClass.getName)
    insert.setObject(5, jsonSerializer.toJson(event))
    insert.executeUpdate

    val update = connection.prepareStatement("UPDATE aggregates SET version = version + 1 WHERE uid = ?")
    update.setLong(1, event.entityId.uid)
    update.executeUpdate

    callAllListenersAboutNewEvent(event.entityClass, event.entityId)

  }

  def addEventForNewAggregate(id: UID, event: CqrsEvent) {

    event.entityId = id

    val insertAggregate = connection.prepareStatement("INSERT INTO aggregates (class, uid, version) VALUES (?,?,?)")
    insertAggregate.setString(1, event.entityClass.getName)
    insertAggregate.setLong(2, id.uid)
    insertAggregate.setInt(3, 1)
    insertAggregate.executeUpdate

    val insertEvent = connection.prepareStatement("INSERT INTO events (aggregate_uid, event_time, version, event_type, event) VALUES (?, ?, ?, ?, ?)")
    insertEvent.setLong(1, event.entityId.uid)
    insertEvent.setTimestamp(2, new Timestamp(new Date().getTime))
    insertEvent.setInt(3, event.expectedVersion)
    insertEvent.setString(4, event.getClass.getName)
    insertEvent.setObject(5, jsonSerializer.toJson(event))
    insertEvent.executeUpdate

    callAllListenersAboutNewEvent(event.entityClass, event.entityId)

  }

  def initDatabaseIfNotExists {

    val createEvents = connection.prepareStatement("CREATE TABLE IF NOT EXISTS events (aggregate_uid BIGINT, event_time TIMESTAMP, version INT, event_type VARCHAR(128), event VARCHAR(10240))")
    createEvents.execute()

    val createAggregates = connection.prepareStatement("CREATE TABLE IF NOT EXISTS aggregates (class VARCHAR(128), uid BIGINT, version INT)")
    createAggregates.execute()

    val createUids = connection.prepareStatement("CREATE TABLE IF NOT EXISTS uids(uidName VARCHAR(128), uid BIGINT)")
    createUids.execute()
  }

  def callListenersForAllAggregates {
    val selectAggregates = connection.prepareStatement("SELECT class, uid FROM aggregates")
    val results = selectAggregates.executeQuery
    while (results.next()) {
      var className = results.getString(1)
      var uid = results.getLong(2)
      callAllListenersAboutNewEvent(Class.forName(className).asInstanceOf[Class[_ <: CqrsEntity]], new UID(uid))
    }
  }


  def addListener(listener: EventStoreListener) {
    listeners += listener
  }

  private def callAllListenersAboutNewEvent(entityClass: Class[_ <: CqrsEntity], entityId: UID) {
    listeners.foreach(listener =>  {
      listener.onEntityChanged(entityClass, entityId)
    })
  }
}
