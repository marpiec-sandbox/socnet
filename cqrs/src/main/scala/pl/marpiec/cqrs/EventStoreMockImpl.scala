package pl.marpiec.cqrs

import exception.{NoEventsForEntityException, NoEventsForTypeException, ConcurrentAggregateModificationException}
import collection.mutable.{ListBuffer, Map, HashMap}
import pl.marpiec.util.UID

class EventStoreMockImpl extends EventStore {

  private val eventsByType = new HashMap[Class[_], Map[UID, ListBuffer[EventRow]]]

  private val listeners = new ListBuffer[EventStoreListener]

  def addEventForNewAggregate(id: UID, event: EventRow) {
    var eventsForType = eventsByType.getOrElseUpdate(event.event.entityClass, new HashMap[UID, ListBuffer[EventRow]])
    var eventsForEntity = eventsForType.getOrElseUpdate(id, new ListBuffer[EventRow])
    event.aggregateId = id
    eventsForEntity += event
    callAllListenersAboutNewEvent(event.event.entityClass, event.aggregateId)
  }

  def getEventsForEntity(entityClass: Class[_], id: UID): ListBuffer[EventRow] = {

    eventsByType.
      get(entityClass).getOrElse(throw new NoEventsForTypeException).
      get(id).getOrElse(throw new NoEventsForEntityException)
  }


  def getAllEventsByType(entityClass: Class[_]): ListBuffer[EventRow] = {

    var events = new ListBuffer[EventRow]()

    var eventsForAggregates = eventsByType.get(entityClass).getOrElse(throw new NoEventsForTypeException).values

    eventsForAggregates.foreach((eventsForAggregate: ListBuffer[EventRow]) => {
      eventsForAggregate.foreach((eventForAggregate: EventRow) => {
        events.append(eventForAggregate)
      })

    })
    events
  }

  def addEventIgnoreVersion(event: EventRow) {
    addEventWithVersionCheck(event, false)
  }

  def addEvent(event: EventRow) {
    addEventWithVersionCheck(event, true)
  }

  private def addEventWithVersionCheck(event: EventRow, checkVersion: Boolean) {
    var eventsForType = eventsByType.getOrElse(event.event.entityClass, null)
    var eventsForEntity = eventsForType.getOrElseUpdate(event.aggregateId, new ListBuffer[EventRow])
    if (!checkVersion) {
      event.expectedVersion = eventsForEntity.size
    }
    if (eventsForEntity.size > event.expectedVersion) {
      throw new ConcurrentAggregateModificationException("Expected version " + event.expectedVersion + " but is " + eventsForEntity.size)
    }

    eventsForEntity += event
    callAllListenersAboutNewEvent(event.event.entityClass, event.aggregateId)
  }


  def callListenersForAllAggregates() {
    //not necessary for mock event store
  }

  def addListener(listener: EventStoreListener) {
    listeners += listener
  }

  private def callAllListenersAboutNewEvent(entityClass: Class[_ <: Aggregate], entityId: UID) {
    listeners.foreach(listener => listener.onEntityChanged(entityClass, entityId))
  }

  @deprecated
  def updateEvent(eventRow: EventRow) {
    //not necessary for mock event store
  }
}
