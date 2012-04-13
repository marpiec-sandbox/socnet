package pl.marpiec.cqrs

import pl.marpiec.util.UID

abstract class CqrsEntity(var id:UID, var version:Int) extends Serializable {
  def copy:CqrsEntity

  def incrementVersion {
    version = version + 1
  }
}
