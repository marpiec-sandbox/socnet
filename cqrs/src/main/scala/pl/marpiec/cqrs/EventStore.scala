package pl.marpiec.cqrs

import collection.mutable.ListBuffer
import pl.marpiec.util.UID


trait EventStore {

  def getEventsForEntity(entityClass: Class[_], id: UID): ListBuffer[Event]

  def addEvent(event: Event)

  def addEventForNewAggregate(newAggregadeId: UID, event: Event)

  def addListener(listener: EventStoreListener)

  def callListenersForAllAggregates

}
