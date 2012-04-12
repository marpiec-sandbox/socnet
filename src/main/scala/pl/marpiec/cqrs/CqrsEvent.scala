package pl.marpiec.cqrs

import java.util.Date
import pl.marpiec.util.UID


abstract class CqrsEvent(var entityId:UID, val expectedVersion:Int, val entityClass:Class[_ <: CqrsEntity]) {

  val timeOccured = new Date

  def applyEvent(entity:CqrsEntity)

}
