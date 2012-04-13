package pl.marpiec.cqrs

import exception.{NoEventsForEntityException, NoEventsForTypeException, ConcurrentAggregateModificationException}
import collection.mutable.{ListBuffer, Map, HashMap}
import pl.marpiec.util.UID

class EventStoreMockImpl extends EventStore {

  private val eventsByType = new HashMap[Class[_], Map[UID, ListBuffer[CqrsEvent]]]

  private val listeners = new ListBuffer[EventStoreListener]

  def addEventForNewAggregate(id:UID, event: CqrsEvent) {
    var eventsForType = eventsByType.getOrElseUpdate(event.entityClass, new HashMap[UID, ListBuffer[CqrsEvent]])
    var eventsForEntity = eventsForType.getOrElseUpdate(id, new ListBuffer[CqrsEvent])
    event.entityId = id
    eventsForEntity += event
    callAllListenersAboutNewEvent(event.entityClass, event.entityId)
  }

  def getEventsForEntity(entityClass: Class[_], id: UID): ListBuffer[CqrsEvent] = {

    eventsByType.
      get(entityClass).getOrElse(throw new NoEventsForTypeException).
      get(id).getOrElse(throw new NoEventsForEntityException)
  }

  def addEvent(event: CqrsEvent) {
    var eventsForType = eventsByType.getOrElse(event.entityClass, null)
    var eventsForEntity = eventsForType.getOrElseUpdate(event.entityId, new ListBuffer[CqrsEvent])
    if (eventsForEntity.size > event.expectedVersion) {
      throw new ConcurrentAggregateModificationException
    }
    eventsForEntity += event
    callAllListenersAboutNewEvent(event.entityClass, event.entityId)
  }

  def initDatabaseIfNotExists {
    //not necessary for mock event store
  }

  def callListenersForAllAggregates {
    //not necessary for mock event store
  }

  def addListener(listener: EventStoreListener) {
    listeners += listener
  }
  
  private def callAllListenersAboutNewEvent(entityClass:Class[_ <: CqrsEntity], entityId:UID) {
    listeners.foreach(listener => listener.onEntityChanged(entityClass, entityId))
  }
}
