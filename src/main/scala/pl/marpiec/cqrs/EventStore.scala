package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, Map}
import java.util.UUID


trait EventStore {

  def getEventsForEntity(entityClass: Class[_], uuid: UUID):ListBuffer[CqrsEvent]

  def addEvent(event: CqrsEvent)

  def addEventForNewAggregate(uuid:UUID, event: CqrsEvent)

  def getEvents(entityClass: Class[_]): Map[UUID, ListBuffer[CqrsEvent]]

  def addListener(listener: EventStoreListener)

}
