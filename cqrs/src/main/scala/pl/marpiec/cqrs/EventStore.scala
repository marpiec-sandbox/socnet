package pl.marpiec.cqrs

import collection.mutable.ListBuffer
import pl.marpiec.util.UID


trait EventStore {

  def getEventsForEntity(entityClass: Class[_], id: UID): ListBuffer[DatabaseEvent]

  def addEvent(event: DatabaseEvent)

  def addEventForNewAggregate(newAggregadeId: UID, event: DatabaseEvent)

  def addListener(listener: EventStoreListener)

  def callListenersForAllAggregates

}
