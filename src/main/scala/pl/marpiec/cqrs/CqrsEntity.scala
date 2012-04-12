package pl.marpiec.cqrs

import java.util.UUID

abstract class CqrsEntity(var uuid:UUID, var version:Int) {
  def copy:CqrsEntity
}
