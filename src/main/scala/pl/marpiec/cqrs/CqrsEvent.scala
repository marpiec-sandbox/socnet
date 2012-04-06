package pl.marpiec.cqrs

import java.util.Date

abstract class CqrsEvent(var entityId:Int, val expectedVersion:Int, val entityClass:Class[_ <: CqrsEntity]) {

  val timeOccured = new Date

  def applyEvent(entity:CqrsEntity)

}
