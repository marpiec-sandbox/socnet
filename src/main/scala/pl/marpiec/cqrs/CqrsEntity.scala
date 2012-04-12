package pl.marpiec.cqrs

import pl.marpiec.util.UID

abstract class CqrsEntity(var id:UID, var version:Int) {
  def copy:CqrsEntity
}
