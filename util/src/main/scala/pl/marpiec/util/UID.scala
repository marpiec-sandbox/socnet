package pl.marpiec.util


/**
 * @author Marcin Pieciukiewicz
 */

class UID(val uid: Long) extends Serializable with Comparable[UID] {

  def compareTo(o: UID) = uid.compareTo(o.uid)

  def nextUid: UID = {
    new UID(uid + 1)
  }

  override def toString = uid.toString

  override def hashCode() = uid.hashCode()

  override def equals(obj: Any) = {
    if(obj.isInstanceOf[UID]) {
      val objUid: UID = obj.asInstanceOf[UID]
      objUid.uid.equals(uid)
    } else {
      false
    }
  }
}

object UID {
  def parseOrZero(str:String):UID = {
    try {
      new UID(str.toLong)
    } catch {
      case e:NumberFormatException => new UID(0)
    }
    throw new IllegalStateException("It shouldn't happen , ever.")
  }
}


/*class UID private(val uuid: UUID) extends Serializable with Comparable[UID] {

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

def parseOr(str: String): UID = {
new UID(UUID.fromString(str))
}

def fromUUID(uuid:UUID): UID = {
new UID(uuid)
}

}     */
