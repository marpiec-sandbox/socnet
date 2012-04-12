package pl.marpiec.cqrs

import java.util.{UUID, Date}


abstract class CqrsEvent(var entityUuid:UUID, val expectedVersion:Int, val entityClass:Class[_ <: CqrsEntity]) {

  val timeOccured = new Date

  def applyEvent(entity:CqrsEntity)

}
