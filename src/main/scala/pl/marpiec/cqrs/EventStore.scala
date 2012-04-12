package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, Map}
import pl.marpiec.util.UID


trait EventStore {

  def getEventsForEntity(entityClass: Class[_], id: UID):ListBuffer[CqrsEvent]

  def addEvent(event: CqrsEvent)

  def addEventForNewAggregate(id:UID, event: CqrsEvent)

  def getEvents(entityClass: Class[_]): Map[UID, ListBuffer[CqrsEvent]]

  def addListener(listener: EventStoreListener)

}
