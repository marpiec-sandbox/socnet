package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, Map}
import pl.marpiec.util.UID


trait EventStore {

  def getEventsForEntity(entityClass: Class[_], id: UID):ListBuffer[CqrsEvent]

  def addEvent(event: CqrsEvent)

  def addEventForNewAggregate(id:UID, event: CqrsEvent)

  def addListener(listener: EventStoreListener)

  def callListenersForAllAggregates

  def initDatabaseIfNotExists

}
