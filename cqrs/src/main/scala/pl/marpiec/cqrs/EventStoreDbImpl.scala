package pl.marpiec.cqrs

import scala.collection.JavaConversions._
import exception.ConcurrentAggregateModificationException
import java.util.Date
import pl.marpiec.util.{JsonSerializer, UID}
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import java.sql.{Timestamp, ResultSet}
import org.springframework.jdbc.core.{RowCallbackHandler, RowMapper, JdbcTemplate}
import collection.mutable.ListBuffer

/**
 * @author Marcin Pieciukiewicz
 */

@Repository("eventStore")
class EventStoreDbImpl @Autowired() (val jdbcTemplate:JdbcTemplate) extends EventStore {

  val DEFAULT_VERSION = 1

  private var listeners = List[EventStoreListener]()

  var jsonSerializer = new JsonSerializer

  val SELECT_EVENTS_QUERY = "SELECT user_uid, aggregate_uid, version, event, event_type " +
    "FROM events " +
    "WHERE aggregate_uid = ? " +
    "ORDER BY event_time"

  val eventRowRowMapper = new RowMapper[EventRow] {
    def mapRow(resultSet: ResultSet, rowNum: Int) = {
      val userId = resultSet.getLong(1)
      val aggregateId = resultSet.getLong(2)
      val version = resultSet.getInt(3)
      val event = resultSet.getString(4)
      val eventType = resultSet.getString(5)
      new EventRow(new UID(userId), new UID(aggregateId), version,
        jsonSerializer.fromJson(event, Class.forName(eventType)).asInstanceOf[Event])
    }
  }

  def getEventsForEntity(entityClass: Class[_], id: UID): ListBuffer[EventRow] = {
    val list = ListBuffer[EventRow]()
    list.addAll(jdbcTemplate.query(SELECT_EVENTS_QUERY, eventRowRowMapper, Array(Long.box(id.uid))).toList)
    list
  }

  def addEventIgnoreVersion(event: EventRow) {
    addEventWithVersionCheck(event, false)
  }

  def addEvent(event: EventRow) {
    addEventWithVersionCheck(event, true)
  }

  private def addEventWithVersionCheck(event: EventRow, checkVersion:Boolean) {


    val currentVersion = jdbcTemplate.queryForInt("SELECT version FROM aggregates WHERE uid = ? AND class = ?",
                                Array(Long.box(event.aggregateId.uid), event.event.entityClass.getName):_*)

    if (currentVersion == 0) {
      throw new IllegalStateException("No aggregate found! ")
    }

    if(!checkVersion) {
      event.expectedVersion = currentVersion
    }

    if (currentVersion > event.expectedVersion) {
      throw new ConcurrentAggregateModificationException
    }

    jdbcTemplate.update("INSERT INTO events (id, user_uid, aggregate_uid, event_time, version, event_type, event) " +
      "VALUES (NEXTVAL('events_seq'), ?, ?, ?, ?, ?, ?)",
      Array(Long.box(event.userId.uid), Long.box(event.aggregateId.uid),
        new Timestamp(new Date().getTime), Int.box(event.expectedVersion), event.event.getClass.getName,
        jsonSerializer.toJson(event.event)):_*)

    jdbcTemplate.update("UPDATE aggregates SET version = version + 1 WHERE uid = ?",
      Array(Long.box(event.aggregateId.uid)):_*)

    callAllListenersAboutNewEvent(event.event.entityClass, event.aggregateId)

  }

  def addEventForNewAggregate(newAggregadeId: UID, event: EventRow) {

    event.aggregateId = newAggregadeId

    jdbcTemplate.update("INSERT INTO aggregates (id, class, uid, version) VALUES (NEXTVAL('aggregates_seq'), ?,?,?)",
    Array(event.event.entityClass.getName, Long.box(newAggregadeId.uid), Int.box(DEFAULT_VERSION)):_*)

    jdbcTemplate.update("INSERT INTO events (id, user_uid, aggregate_uid, event_time, version, event_type, event) " +
      "VALUES (NEXTVAL('events_seq'), ?, ?, ?, ?, ?, ?)",
        Array(Long.box(event.userId.uid),
          Long.box(event.aggregateId.uid),
          new Timestamp(new Date().getTime),
          Int.box(event.expectedVersion),
          event.event.getClass.getName,
          jsonSerializer.toJson(event.event)):_*)

    callAllListenersAboutNewEvent(event.event.entityClass, event.aggregateId)

  }

  def callListenersForAllAggregates {

    jdbcTemplate.query("SELECT class, uid FROM aggregates", new RowCallbackHandler {
      def processRow(rs: ResultSet) {
        var className = rs.getString(1)
        var uid = rs.getLong(2)
        callAllListenersAboutNewEvent(Class.forName(className).asInstanceOf[Class[_ <: Aggregate]], new UID(uid))
      }
    })

  }


  def addListener(listener: EventStoreListener) {
    listeners ::= listener
  }

  private def callAllListenersAboutNewEvent(entityClass: Class[_ <: Aggregate], entityId: UID) {
    listeners.foreach(listener => {
      listener.onEntityChanged(entityClass, entityId)
    })
  }
}
