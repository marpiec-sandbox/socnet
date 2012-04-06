package pl.marpiec.cqrs

import exception.{NoEventsForEntityException, NoEventsForTypeException, ConcurrentAggregateModificationException}
import collection.mutable.{ListBuffer, Map, HashMap}

class EventStoreImpl extends EventStore {

  private val eventsByType = new HashMap[Class[_], Map[Int, ListBuffer[CqrsEvent]]]

  private val listeners = new ListBuffer[EventStoreListener]

  def getEvents(entityClass: Class[_]) = eventsByType.getOrElse(entityClass, null)

  def addEventForNewAggregate(event: CqrsEvent) = {

    var eventsForType = eventsByType.getOrElseUpdate(event.entityClass, new HashMap[Int, ListBuffer[CqrsEvent]])
    val newKey = eventsForType.size + 1
    event.entityId = newKey
    var eventsForEntity = eventsForType.getOrElseUpdate(newKey, new ListBuffer[CqrsEvent])
    eventsForEntity += event
    callAllListenersAboutNewEvent(event)
    newKey
  }

  def getEventsForEntity(entityClass: Class[_], id: Int): ListBuffer[CqrsEvent] = {

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
  }

  def addListener(listener: EventStoreListener) {
    listeners += listener
  }
  
  private def callAllListenersAboutNewEvent(event: CqrsEvent) {
    listeners.foreach(listener => listener.onNewEvent(event))
  }
}
