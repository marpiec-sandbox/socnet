package pl.marpiec.cqrs

import pl.marpiec.util.UID


trait EventStore {

  def getEventsForEntity(entityClass: Class[_], id: UID): List[EventRow]

  def addEvent(event: EventRow)

  def addEventForNewAggregate(newAggregadeId: UID, event: EventRow)

  def addListener(listener: EventStoreListener)

  def callListenersForAllAggregates

}
