package pl.marpiec.cqrs

import exception.{NoEventsForEntityException, NoEventsForTypeException, ConcurrentAggregateModificationException}
import collection.mutable.{ListBuffer, Map, HashMap}
import pl.marpiec.util.UID

class EventStoreMockImpl extends EventStore {

  private val eventsByType = new HashMap[Class[_], Map[UID, List[EventRow]]]

  private val listeners = new ListBuffer[EventStoreListener]

  def addEventForNewAggregate(id:UID, event: EventRow) {
    var eventsForType = eventsByType.getOrElseUpdate(event.event.entityClass, new HashMap[UID, List[EventRow]])
    var eventsForEntity = eventsForType.getOrElseUpdate(id, List[EventRow]())
    event.aggregateId = id
    eventsForEntity ::= event
    callAllListenersAboutNewEvent(event.event.entityClass, event.aggregateId)
  }

  def getEventsForEntity(entityClass: Class[_], id: UID): List[EventRow] = {

    eventsByType.
      get(entityClass).getOrElse(throw new NoEventsForTypeException).
      get(id).getOrElse(throw new NoEventsForEntityException)
  }

  def addEvent(event: EventRow) {
    var eventsForType = eventsByType.getOrElse(event.event.entityClass, null)
    var eventsForEntity = eventsForType.getOrElseUpdate(event.aggregateId, List[EventRow]())
    if (eventsForEntity.size > event.expectedVersion) {
      throw new ConcurrentAggregateModificationException
    }
    eventsForEntity ::= event
    callAllListenersAboutNewEvent(event.event.entityClass, event.aggregateId)
  }


  def callListenersForAllAggregates {
    //not necessary for mock event store
  }

  def addListener(listener: EventStoreListener) {
    listeners += listener
  }
  
  private def callAllListenersAboutNewEvent(entityClass:Class[_ <: Aggregate], entityId:UID) {
    listeners.foreach(listener => listener.onEntityChanged(entityClass, entityId))
  }
}
