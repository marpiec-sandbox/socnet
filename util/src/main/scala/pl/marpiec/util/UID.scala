package pl.marpiec.util

import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

class UID private(val uuid: UUID) extends Serializable with Comparable[UID] {

  def compareTo(o: UID) = uuid.compareTo(o.uuid)

  override def toString = uuid.toString

  override def hashCode() = uuid.hashCode()

  override def equals(obj: Any) = {
    if(obj.isInstanceOf[UID]) {
      val uid: UID = obj.asInstanceOf[UID]
      uuid.equals(uid.uuid)
    } else {
      false
    }
  }
}

object UID {

  def generate: UID = {
    new UID(UUID.randomUUID)
  }

  def parse(str: String): UID = {
    new UID(UUID.fromString(str))
  }

  def fromUUID(uuid:UUID): UID = {
    new UID(uuid)
  }

}
