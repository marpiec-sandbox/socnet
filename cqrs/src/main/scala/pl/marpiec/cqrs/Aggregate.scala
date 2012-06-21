package pl.marpiec.cqrs

import pl.marpiec.util.UID

abstract class Aggregate(var id: UID, var version: Int) extends Serializable {
  def copy: Aggregate

  def incrementVersion() {
    version = version + 1
  }
}
