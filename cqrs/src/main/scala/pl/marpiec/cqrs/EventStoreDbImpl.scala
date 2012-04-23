package pl.marpiec.cqrs

import collection.mutable.ListBuffer
import exception.ConcurrentAggregateModificationException
import java.sql._
import java.util.Date
import pl.marpiec.util.{JsonUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class EventStoreDbImpl extends EventStore {

  private val connection: Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
  private val listeners = new ListBuffer[EventStoreListener]

  var jsonSerializer = new JsonUtil

  def getEventsForEntity(entityClass: Class[_], id: UID): ListBuffer[DatabaseEvent] = {

    val selectEvents = connection.prepareStatement("SELECT user_uid, aggregate_uid, version, event, event_type FROM events WHERE aggregate_uid = ? ORDER BY event_time")
    selectEvents.setLong(1, id.uid)
    val results = selectEvents.executeQuery

    val events = new ListBuffer[DatabaseEvent]

    while (results.next()) {

      var userId = results.getLong(1)
      var aggregateId = results.getLong(2)
      var version = results.getInt(3)
      var event = results.getString(4)
      var eventType = results.getString(5)

      events += new DatabaseEvent(new UID(userId), new UID(aggregateId), version, jsonSerializer.fromJson(event, Class.forName(eventType)).asInstanceOf[CqrsEvent])

    }

    events
  }

  def addEvent(event: DatabaseEvent) {

    val selectAggregateVersion = connection.prepareStatement("SELECT version FROM aggregates WHERE uid = ? AND class = ?")
    selectAggregateVersion.setLong(1, event.aggregateId.uid)
    selectAggregateVersion.setString(2, event.event.entityClass.getName)

    val versionResults: ResultSet = selectAggregateVersion.executeQuery()

    if (versionResults.next) {
      val currentVersion: Int = versionResults.getInt(1)
      if (currentVersion > event.expectedVersion) {
        throw new ConcurrentAggregateModificationException
      }
    } else {
      throw new IllegalStateException("No aggregate found! ")
    }

    val insert = connection.prepareStatement("INSERT INTO events (id, user_uid, aggregate_uid, event_time, version, event_type, event) VALUES (NEXTVAL('events_seq'), ?, ?, ?, ?, ?, ?)")
    insert.setLong(1, event.userId.uid)
    insert.setLong(2, event.aggregateId.uid)
    insert.setTimestamp(3, new Timestamp(new Date().getTime))
    insert.setInt(4, event.expectedVersion)
    insert.setString(5, event.event.getClass.getName)
    insert.setObject(6, jsonSerializer.toJson(event.event))
    insert.executeUpdate


    val update = connection.prepareStatement("UPDATE aggregates SET version = version + 1 WHERE uid = ?")
    update.setLong(1, event.aggregateId.uid)
    update.executeUpdate

    callAllListenersAboutNewEvent(event.event.entityClass, event.aggregateId)

  }

  def addEventForNewAggregate(newAggregadeId: UID, event: DatabaseEvent) {

    event.aggregateId = newAggregadeId

    val insertAggregate = connection.prepareStatement("INSERT INTO aggregates (id, class, uid, version) VALUES (NEXTVAL('aggregates_seq'), ?,?,?)")
    insertAggregate.setString(1, event.event.entityClass.getName)
    insertAggregate.setLong(2, newAggregadeId.uid)
    insertAggregate.setInt(3, 1)
    insertAggregate.executeUpdate

    val insertEvent = connection.prepareStatement("INSERT INTO events (id, user_uid, aggregate_uid, event_time, version, event_type, event) VALUES (NEXTVAL('events_seq'), ?, ?, ?, ?, ?, ?)")
    insertEvent.setLong(1, event.userId.uid)
    insertEvent.setLong(2, event.aggregateId.uid)
    insertEvent.setTimestamp(3, new Timestamp(new Date().getTime))
    insertEvent.setInt(4, event.expectedVersion)
    insertEvent.setString(5, event.event.getClass.getName)
    insertEvent.setObject(6, jsonSerializer.toJson(event.event))
    insertEvent.executeUpdate

    callAllListenersAboutNewEvent(event.event.entityClass, event.aggregateId)

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
    listeners.foreach(listener => {
      listener.onEntityChanged(entityClass, entityId)
    })
  }
}
