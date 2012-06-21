package pl.marpiec.cqrs

import pl.marpiec.util.UID
import collection.mutable.ListBuffer


trait EventStore {

  def getEventsForEntity(entityClass: Class[_], id: UID): ListBuffer[EventRow]

  def getAllEventsByType(entityClass: Class[_]): ListBuffer[EventRow]

  def addEventIgnoreVersion(event: EventRow)

  def addEvent(event: EventRow)

  /**Use only for migrating events, not for normal business!!! */
  @deprecated
  def updateEvent(eventRow: EventRow)

  def addEventForNewAggregate(newAggregadeId: UID, event: EventRow)

  def addListener(listener: EventStoreListener)

  def callListenersForAllAggregates()

}
