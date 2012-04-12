package pl.marpiec.cqrs

import exception.{NoEventsForEntityException, NoEventsForTypeException, ConcurrentAggregateModificationException}
import collection.mutable.{ListBuffer, Map, HashMap}
import pl.marpiec.util.UID

class EventStoreImpl extends EventStore {

  private val eventsByType = new HashMap[Class[_], Map[UID, ListBuffer[CqrsEvent]]]

  private val listeners = new ListBuffer[EventStoreListener]

  def getEvents(entityClass: Class[_]) = eventsByType.getOrElse(entityClass, null)

  def addEventForNewAggregate(id:UID, event: CqrsEvent) {
    var eventsForType = eventsByType.getOrElseUpdate(event.entityClass, new HashMap[UID, ListBuffer[CqrsEvent]])
    var eventsForEntity = eventsForType.getOrElseUpdate(id, new ListBuffer[CqrsEvent])
    event.entityId = id
    eventsForEntity += event
    callAllListenersAboutNewEvent(event)
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
    callAllListenersAboutNewEvent(event)
  }

  def addListener(listener: EventStoreListener) {
    listeners += listener
  }
  
  private def callAllListenersAboutNewEvent(event: CqrsEvent) {
    listeners.foreach(listener => listener.onNewEvent(event))
  }
}
