package pl.marpiec.cqrs

import exception.{NoEventsForEntityException, NoEventsForTypeException, ConcurrentAggregateModificationException}
import collection.mutable.{ListBuffer, Map, HashMap}

class EventStoreImpl extends EventStore {

  val eventsByType = new HashMap[Class[_], Map[Int, ListBuffer[CqrsEvent]]]

  def getEvents(entityClass: Class[_]) = eventsByType.getOrElse(entityClass, null)

  def addEventForNewAggregate(event: CqrsEvent) = {

    var eventsForType = eventsByType.getOrElseUpdate(event.entityClass, new HashMap[Int, ListBuffer[CqrsEvent]])
    val newKey = eventsForType.size + 1
    var eventsForEntity = eventsForType.getOrElseUpdate(newKey, new ListBuffer[CqrsEvent])
    eventsForEntity += event
    newKey
  }

  def getEventsForEntity(entityClass: Class[_], id: Int): ListBuffer[CqrsEvent] = {

    eventsByType.
      get(entityClass).getOrElse(throw new NoEventsForTypeException).
      get(id).getOrElse(throw new NoEventsForEntityException)
  }

  def addEvent(id: Int, version: Int, event: CqrsEvent) {
    var eventsForType = eventsByType.getOrElse(event.entityClass, null)
    var eventsForEntity = eventsForType.getOrElseUpdate(id, new ListBuffer[CqrsEvent])
    if (eventsForEntity.size > version) {
      throw new ConcurrentAggregateModificationException
    }
    eventsForEntity += event
  }
}
