package pl.marpiec.cqrs

import exception.{NoEventsForEntityException, NoEventsForTypeException, ConcurrentAggregateModificationException}
import collection.mutable.{ListBuffer, Map, HashMap}
import java.util.UUID

class EventStoreImpl extends EventStore {

  private val eventsByType = new HashMap[Class[_], Map[UUID, ListBuffer[CqrsEvent]]]

  private val listeners = new ListBuffer[EventStoreListener]

  def getEvents(entityClass: Class[_]) = eventsByType.getOrElse(entityClass, null)

  def addEventForNewAggregate(uuid:UUID, event: CqrsEvent) {
    var eventsForType = eventsByType.getOrElseUpdate(event.entityClass, new HashMap[UUID, ListBuffer[CqrsEvent]])
    var eventsForEntity = eventsForType.getOrElseUpdate(uuid, new ListBuffer[CqrsEvent])
    event.entityUuid = uuid
    eventsForEntity += event
    callAllListenersAboutNewEvent(event)
  }

  def getEventsForEntity(entityClass: Class[_], uuid: UUID): ListBuffer[CqrsEvent] = {

    eventsByType.
      get(entityClass).getOrElse(throw new NoEventsForTypeException).
      get(uuid).getOrElse(throw new NoEventsForEntityException)
  }

  def addEvent(event: CqrsEvent) {
    var eventsForType = eventsByType.getOrElse(event.entityClass, null)
    var eventsForEntity = eventsForType.getOrElseUpdate(event.entityUuid, new ListBuffer[CqrsEvent])
    if (eventsForEntity.size > event.expectedVersion) {
      throw new ConcurrentAggregateModificationException
    }
    eventsForEntity += event
    callAllListenersAboutNewEvent(event)
  }

  def addListener(listener: EventStoreListener) {
    listeners += listener
  }
  
  private def callAllListenersAboutNewEvent(event: CqrsEvent) {
    listeners.foreach(listener => listener.onNewEvent(event))
  }
}
