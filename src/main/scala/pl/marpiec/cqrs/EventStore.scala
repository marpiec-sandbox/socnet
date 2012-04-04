package pl.marpiec.cqrs

import collection.mutable.{ListBuffer, Map}


trait EventStore {


  def getEventsForEntity(entityClass: Class[_], i: Int):ListBuffer[CqrsEvent]

  def addEvent(id: Int, version:Int, event: CqrsEvent)

  def addEventForNewAggregate(event: CqrsEvent): Int

  def getEvents(entityClass: Class[_]): Map[Int, ListBuffer[CqrsEvent]]

}
