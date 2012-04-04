package pl.marpiec.cqrs

import java.util.Date

abstract class CqrsEvent(val entityId:Int, val expectedVersion:Int, val entityClass:Class[_]) {

  val timeOccured = new Date

  def applyEvent(entity:CqrsEntity)

}
